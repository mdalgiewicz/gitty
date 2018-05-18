package pl.dalgim.gitty;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Mateusz Dalgiewicz
 */
public abstract class TestCase {

  public String readFile(String fileName) throws URISyntaxException, IOException {
    StringBuilder buffer = new StringBuilder();
    final URI uri = TestCase.class
        .getClassLoader()
        .getResource(fileName)
        .toURI();
    Files.lines(Paths.get(uri))
        .forEach(buffer::append);
    return buffer.toString();
  }

}
