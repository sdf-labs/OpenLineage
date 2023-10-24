/*
/* Copyright 2018-2023 contributors to the OpenLineage project
/* SPDX-License-Identifier: Apache-2.0
*/

package io.openlineage.client.transports;

public class SdfTransportBuilder implements TransportBuilder {

  @Override
  public TransportConfig getConfig() {
    return new SdfConfig();
  }

  @Override
  public Transport build(TransportConfig config) {
    return new SdfTransport((SdfConfig) config);
  }

  @Override
  public String getType() {
    return "sdf";
  }
}
