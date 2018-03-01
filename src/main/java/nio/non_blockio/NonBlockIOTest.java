package nio.non_blockio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author 唐国翔
 * @date 2018-03-01 10:06
 * <p>
 * 　　　　　　　　┏┓　　　┏┓+ +
 * 　　　　　　　┏┛┻━━━┛┻┓ + +
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃ ++ + + +
 * 　　　　　　 ████━████ ┃+
 * 　　　　　　　┃　　　　　　　┃ +
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　　┃ + +
 * 　　　　　　　┗━┓　　　  ┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃ + + + +
 * 　　　　　　　　　┃　　　┃　　　　Code is far away from bug with the animal protecting
 * 　　　　　　　　　┃　　　┃ + 　　　　神兽保佑,代码无bug
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　+
 * 　　　　　　　　　┃　 　　┗━━━┓ + +
 * 　　　　　　　　　┃ 　　　　　　　┣┓
 * 　　　　　　　　　┃ 　　　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛+ + + +
 **/
public class NonBlockIOTest {
    /**
     * 客户端
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        //获取客户端通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",9898));
        //切换成非阻塞模式
        socketChannel.configureBlocking(false);
        //分配指定大小的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //发送数据给服务端
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()){
            String str = scanner.next();
            buffer.put((LocalDateTime.now().toString() + "\n" + str).getBytes());
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();
        }
        //关闭通道
        socketChannel.close();
    }

    /**
     * 服务端
     */
    @Test
    public void server() throws IOException {
        //获取服务端channel通道
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        //切换非阻塞模式
        ssChannel.configureBlocking(false);
        //绑定连接
        ssChannel.bind(new InetSocketAddress(9898));
        //获取选择器
        Selector selector = Selector.open();
        //将通道注册到选择器中,并且指定监听事件
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);
        //轮询式的获取选择器上已经"准备就绪"的事件
        while (selector.select() > 0){
            //获取当前选择器中所有注册的选择键（已经就绪的监听事件）
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                //获取准备“就绪”的事件
                SelectionKey key = iterator.next();
                //判断具体是什么事件准备就绪
                if(key.isAcceptable()){
                    SocketChannel sChannel = ssChannel.accept();
                    //切换非阻塞模式
                    sChannel.configureBlocking(false);
                    //将该通道注册到选择器中
                    sChannel.register(selector,SelectionKey.OP_READ);
                }else if(key.isReadable()){
                    //获取当前选择器上“读就绪”状态的通道
                    SocketChannel channel = (SocketChannel)key.channel();

                    //读取数据
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int len;
                    while ((len = channel.read(buffer)) > 0){
                        buffer.flip();
                        System.out.println(new String(buffer.array(),0,len));
                        buffer.clear();
                    }
                }
                //取消选择键SelectionKey
                iterator.remove();
            }
        }

    }

}
