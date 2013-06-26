package org.openlegacy.demo.db.services;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShippingService {

	public List<Shipping> getShippings() {

		List<Shipping> shippings = new ArrayList<Shipping>();

		try {
			String shippingsText = IOUtils.toString(getClass().getResource("/shippings.txt"));

			StringReader reader = new StringReader(shippingsText);
			BufferedReader br = new BufferedReader(reader);
			String shippingLine = br.readLine();
			while (shippingLine != null) {

				String[] shippingsCells = shippingLine.split(",");

				Shipping shipping = new Shipping();
				shipping.setName(shippingsCells[0]);
				shipping.setAddress(shippingsCells[1]);
				shipping.setPhone(shippingsCells[2]);
				shipping.setDate(shippingsCells[3]);

				shippings.add(shipping);

				shippingLine = br.readLine();
			}
			return shippings;

		} catch (IOException e) {
			throw (new RuntimeException(e));
		}

	}
}
