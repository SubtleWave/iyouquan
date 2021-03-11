/*
 * This file is part of java-psd-library.
 * 
 * This library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package psd.parser.object;

import java.io.IOException;

import psd.parser.PsdInputStream;

// TODO: Auto-generated Javadoc
/**
 * The Class PsdDouble.
 *
 * @author Dmitry Belsky
 */
public class PsdDouble extends PsdObject {

	/** The value. */
	private final double value;

	/**
	 * Instantiates a new psd double.
	 *
	 * @param stream the stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public PsdDouble(PsdInputStream stream) throws IOException {
		value = stream.readDouble();
		logger.finest("PsdDouble.value: " + value);
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "doub:" + value;
	}

}
