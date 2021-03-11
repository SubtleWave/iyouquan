package psd.parser.header;

import java.io.IOException;

import psd.parser.ColorMode;
import psd.parser.PsdInputStream;

public class HeaderSectionParser {

	private static final String FILE_SIGNATURE = "8BPS";
	private static final int FILE_VERSION = 1;

	private HeaderSectionHandler handler;
	private Header header = new Header();

	public void setHandler(HeaderSectionHandler handler) {
		this.handler = handler;
	}

	public void parse(PsdInputStream psdStream) throws IOException {
		String fileSignature = psdStream.readString(4);
		if (!fileSignature.equals(FILE_SIGNATURE)) {
			throw new IOException("file signature error");
		}

		int ver = psdStream.readShort();
		if (ver != FILE_VERSION) {
			throw new IOException("file version error ");
		}

		psdStream.skipBytes(6); // reserved

		header.channelsCount = psdStream.readShort();
		header.height = psdStream.readInt();
		header.width = psdStream.readInt();
		header.depth = psdStream.readShort();
		int colorModeIndex = psdStream.readShort();
		header.colorMode = ColorMode.values()[colorModeIndex];
		if (handler != null) {
			handler.headerLoaded(header);
		}
	}

	public Header getHeader() {
		return header;
	}
}
