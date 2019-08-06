package hello.world;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class RandomEmailGeneratorTest {

    @Test
    public void testGenerate() {
        RandomEmailGenerator generator=new RandomEmailGenerator();
        String email= generator.generate();

        Assert.assertNotNull(email);
        Assert.assertEquals(email, "jason@gmail.com");
    }
}