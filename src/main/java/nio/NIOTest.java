package nio;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * nio测试类
 *
 * @author 唐国翔
 * @date 2018-01-16 9:50
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
public class NIOTest {

    /**
     * 基本概念
     */
    @Test
    public void test0(){
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //缓冲区容量
        System.out.println(buffer.capacity());
        //缓冲区可操作的大小
        System.out.println(buffer.limit());
        //缓冲区当前操作位置
        System.out.println(buffer.position());

        buffer.put("abcdefg".getBytes());
        //缓冲区容量
        System.out.println(buffer.capacity());
        //缓冲区可操作的大小
        System.out.println(buffer.limit());
        //缓冲区当前操作位置
        System.out.println(buffer.position());

        buffer.flip();
        byte[] bytes = new byte[10];

        System.out.println(new String(bytes,0,bytes.length));

    }

    /**
     * 直接缓冲区复制文件
     * try-catch-resource
     *
     */
    @Test
    public void test1(){
        try(FileInputStream fis = new FileInputStream("1.jpg");
            FileOutputStream fos = new FileOutputStream("2.jpg");
            FileChannel inChannel = fis.getChannel();
            FileChannel outChannel = fos.getChannel()) {

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
            while (inChannel.read(byteBuffer) != -1){
                byteBuffer.flip();
                outChannel.write(byteBuffer);
                byteBuffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 非直接缓冲区复制文件
     */
    @Test
    public void test2(){
        try(FileInputStream fis = new FileInputStream("1.jpg");
            FileOutputStream fos = new FileOutputStream("2.jpg");
            FileChannel inChannel = fis.getChannel();
            FileChannel outChannel = fos.getChannel()) {

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            while (inChannel.read(byteBuffer) != -1){
                byteBuffer.flip();
                outChannel.write(byteBuffer);
                byteBuffer.clear();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 内存映射文件
     */
    @Test
    public void test3(){
        try (FileChannel inChannel = FileChannel.open(Paths.get("1.jpg"),StandardOpenOption.READ);
             FileChannel outChannel = FileChannel.open(Paths.get("2.jpg"),StandardOpenOption.READ, StandardOpenOption.WRITE,StandardOpenOption.CREATE)){

            MappedByteBuffer inMap = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
            MappedByteBuffer outMap = outChannel.map(FileChannel.MapMode.READ_WRITE,0,inChannel.size());

            byte[] bytes = new byte[inMap.limit()];
            inMap.get(bytes);
            outMap.put(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 利用channel自带方法进行文件传输
     */
    @Test
    public void test4(){
        try (FileChannel inChannel = FileChannel.open(Paths.get("1.jpg"),StandardOpenOption.READ);
             FileChannel outChannel = FileChannel.open(Paths.get("2.jpg"),StandardOpenOption.READ,StandardOpenOption.WRITE,StandardOpenOption.CREATE)){

            outChannel.transferFrom(inChannel,0,inChannel.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 字符集
     */
    @Test
    public void test5() throws CharacterCodingException {
        /*SortedMap<String, Charset> charsets = Charset.availableCharsets();
        for (Map.Entry<String, Charset> charsetEntry : charsets.entrySet()) {
            System.out.println(charsetEntry.getKey()+" : "+charsetEntry.getValue());
        }*/
        Charset charset = Charset.forName("gbk");
        CharsetEncoder encoder = charset.newEncoder();
        CharsetDecoder decoder = charset.newDecoder();


        CharBuffer charBuffer = CharBuffer.allocate(1024);
        charBuffer.put("发几份接我");

        charBuffer.flip();

        ByteBuffer encode = encoder.encode(charBuffer);

        CharBuffer decode = decoder.decode(encode);
        System.out.println(new String(decode.array()));
    }


    public void test6(){

    }

}
