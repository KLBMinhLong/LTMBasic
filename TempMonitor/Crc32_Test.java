
import java.util.zip.CRC32;

public class Crc32_Test {

    public static long getCRC32Checksum(String data) {
        CRC32 crc32 = new CRC32();
        crc32.update(data.getBytes());
        return crc32.getValue();
    }
}

