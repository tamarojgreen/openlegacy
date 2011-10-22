package org.openlegacy.adapter.terminal.trail;

import org.openlegacy.trail.TrailWriter;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class XmlTrailWriter implements TrailWriter<UnifiedTerminalTrail> {

	public void write(UnifiedTerminalTrail trail, OutputStream out) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JAXBContext context = JAXBContext.newInstance(UnifiedTerminalTrail.class, UnifiedTerminalTrailStage.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(trail, baos);
			String s = new String(baos.toByteArray());
			System.out.println(s);
		} catch (Exception e) {
			throw (new RuntimeException(e));
		}

	}
}
