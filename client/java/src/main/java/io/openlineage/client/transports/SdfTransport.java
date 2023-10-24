/*
/* Copyright 2018-2023 contributors to the OpenLineage project
/* SPDX-License-Identifier: Apache-2.0
*/

package io.openlineage.client.transports;

import io.openlineage.client.OpenLineage;
import io.openlineage.client.OpenLineageClientUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Writes a new file for each Openlineage. Files are labeled with
 * {run_id}_{yyyy_MM_dd_HH_mm_ss}.json
 */
@Slf4j
public class SdfTransport extends Transport {

  Path output_dir;
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");

  public SdfTransport(@NonNull final SdfConfig sdfConfig) {
    super(Type.SDF);
    output_dir = Paths.get(sdfConfig.getOutput());
  }

  @Override
  public void emit(OpenLineage.RunEvent runEvent) {
    // if DEBUG loglevel is enabled, this will double-log even due to OpenLineageClient also logging
    emit(
        OpenLineageClientUtils.toJson(runEvent),
        runEvent.getRun().getRunId().toString(),
        runEvent.getEventTime().format(formatter));
  }

  public void emit(String eventAsJson, String runId, String suffix) {
    Path path_to_write = output_dir.resolve(runId + "_" + suffix + ".json");
    try {
      Files.write(path_to_write, eventAsJson.getBytes(StandardCharsets.UTF_8));
      log.info("emitted event: " + eventAsJson);
    } catch (IOException | IllegalArgumentException e) {
      log.error("Writing event to a file {} failed: {}", path_to_write.toString(), e);
    }
  }
}
