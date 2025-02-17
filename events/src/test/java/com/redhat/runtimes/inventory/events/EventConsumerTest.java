/* Copyright (C) Red Hat 2023 */
package com.redhat.runtimes.inventory.events;

import static com.redhat.runtimes.inventory.events.EventConsumer.runtimesInstanceOf;
import static com.redhat.runtimes.inventory.events.Utils.readBytesFromResources;
import static com.redhat.runtimes.inventory.events.Utils.readFromResources;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.runtimes.inventory.models.RuntimesInstance;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class EventConsumerTest {

  @Test
  public void testSimpleUnzip() throws IOException {
    var buffy = readBytesFromResources("1J6DOEu9ni-000029.gz");
    var json = EventConsumer.unzipJson(buffy);
    TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {};
    var mapper = new ObjectMapper();
    var o = mapper.readValue(json, typeRef);
    var idHash =
        "1fe34df6b75eaf557e59f58b88f584f398ed4b73e41730e3e88c34d6052ae231c677bdc1a1c5ce22202c485ce4f101f66fc283c5a7f81f51c59b09806b481f22";
    assertEquals(idHash, o.get("idHash"));
  }

  @Test
  public void test_runtimesInstance_MWTELE_66() throws IOException {
    var dummy = new ArchiveAnnouncement();
    dummy.setTimestamp(LocalDateTime.MAX);

    var buffy = readBytesFromResources("jdk8_MWTELE-66.gz");
    var json = EventConsumer.unzipJson(buffy);

    var msg = runtimesInstanceOf(dummy, json);
    assertTrue(msg instanceof RuntimesInstance);
    var inst = (RuntimesInstance) msg;

    var hostname = "fedora";
    assertEquals(hostname, inst.getHostname());

    json = readFromResources("test17.json");
    msg = runtimesInstanceOf(dummy, json);
    assertTrue(msg instanceof RuntimesInstance);
    inst = (RuntimesInstance) msg;

    hostname = "uriel.local";
    assertEquals(hostname, inst.getHostname());
  }

  @Test
  @Disabled
  public void test_runtimesInstance_MWTELE_67() throws IOException {
    var dummy = new ArchiveAnnouncement();
    dummy.setTimestamp(LocalDateTime.MIN);

    var buffy = readBytesFromResources("update1.json.gz");
    var json = EventConsumer.unzipJson(buffy);
    var msg = runtimesInstanceOf(dummy, json);
    assertTrue(msg instanceof RuntimesInstance);
    var inst = (RuntimesInstance) msg;

    var hostname = "fedora";
    assertEquals(hostname, inst.getHostname());
  }
}
