package org.openlegacy.adapter.terminal.trail;

import org.openlegacy.trail.SessionTrail;
import org.openlegacy.trail.TrailStage;
import org.openlegacy.trail.TrailWriter;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class XmlTrailWriter implements TrailWriter {

	public void write(SessionTrail trail, OutputStream out) {

		UnifiedTerminalTrail unifiedTerminalTrail = new UnifiedTerminalTrail();
		List<TrailStage> stages = trail.getStages();
		for (TrailStage trailStage : stages) {
			unifiedTerminalTrail.appendStage(trailStage);
		}

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JAXBContext context = JAXBContext.newInstance(UnifiedTerminalTrail.class, UnifiedTerminalTrailStage.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(unifiedTerminalTrail, baos);
			String s = new String(baos.toByteArray());
			System.out.println(s);
		} catch (Exception e) {
			throw (new RuntimeException(e));
		}

	}
}
