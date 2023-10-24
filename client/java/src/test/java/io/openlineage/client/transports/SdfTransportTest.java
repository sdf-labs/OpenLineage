/*
/* Copyright 2018-2023 contributors to the OpenLineage project
/* SPDX-License-Identifier: Apache-2.0
*/

package io.openlineage.client.transports;

import io.openlineage.client.OpenLineage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SdfTransportTest {

  private static final String FILE_LOCATION_DIR = "/tmp/openlineage_sdf_transport_test";

  SdfConfig sdfConfig;
  Transport transport;

  @BeforeEach
  @SneakyThrows
  public void beforeEach() {
    FileUtils.deleteDirectory(new File(FILE_LOCATION_DIR));

    sdfConfig = new SdfConfig();
    sdfConfig.setOutput(FILE_LOCATION_DIR);
    transport = new SdfTransport(sdfConfig);
  }

  @Test
  @SneakyThrows
  public void transportWritesToFiles() {
    transport.emit(
        new OpenLineage(URI.create("http://test.producer"))
            .newRunEventBuilder()
            .job(new OpenLineage.JobBuilder().name("test-job").namespace("test-ns").build())
            .run(new OpenLineage.RunBuilder().runId(UUID.randomUUID()).build())
            .eventTime(ZonedDateTime.now())
            .build());
    Path output_path = Paths.get(FILE_LOCATION_DIR);

    try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(output_path)) {
      Path firstFile = directoryStream.iterator().next();
      if (Files.exists(firstFile)) {
        List<String> lines = Files.readAllLines(firstFile);

        // Print the contents of the first file
        for (String line : lines) {
          System.out.println(line);
        }
      } else {
        System.out.println("No files found in the directory.");
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
