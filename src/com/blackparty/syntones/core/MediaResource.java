package com.blackparty.syntones.core;

import java.io.File;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Date;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.IOUtils;

import java.io.*;

public class MediaResource {

	private int chunkSize; // 1MB chunks
	private File audio;

	public MediaResource() {
		this.chunkSize = 1024 * 1024;
	}

	public Response buildStream(final File asset, final String range) throws Exception {
		// range not requested : Firefox, Opera, IE do not send range headers
		System.out.println("Running MediaResource.buildStream();");
		if (range == null) {
            StreamingOutput streamer = new StreamingOutput() {
                @Override
                public void write(final OutputStream output) throws IOException, WebApplicationException {

                    final FileChannel inputChannel = new FileInputStream(asset).getChannel();
                    final WritableByteChannel outputChannel = Channels.newChannel(output);
                    try {
                        inputChannel.transferTo(0, inputChannel.size(), outputChannel);
                    } finally {
                        // closing the channels
                        inputChannel.close();
                        outputChannel.close();
                    }
                }
            };
            return Response.ok(streamer).status(200).header(HttpHeaders.CONTENT_LENGTH, asset.length()).build();
        }

        String[] ranges = range.split("=")[1].split("-");
        final int from = Integer.parseInt(ranges[0]);
        /**
         * Chunk media if the range upper bound is unspecified. Chrome sends "bytes=0-"
         */
        int to = chunkSize + from;
        if (to >= asset.length()) {
            to = (int) (asset.length() - 1);
        }
        if (ranges.length == 2) {
            to = Integer.parseInt(ranges[1]);
        }

        final String responseRange = String.format("bytes %d-%d/%d", from, to, asset.length());
        final RandomAccessFile raf = new RandomAccessFile(asset, "r");
        raf.seek(from);

        
        
        final int len = to - from + 1;
        final MediaStreamer streamer = new MediaStreamer(len, raf);
        Response.ResponseBuilder res = Response.ok(streamer).status(206)
                .header("Accept-Ranges", "bytes")
                .header("Content-Range", responseRange)
                .header(HttpHeaders.CONTENT_LENGTH, streamer.getLength())
                .header(HttpHeaders.LAST_MODIFIED, new Date(asset.lastModified()));
        return res.build();
		
	}

	public void play(String audioFile) throws Exception {
		AudioInputStream stream = AudioSystem.getAudioInputStream(new File(audioFile));
		AudioFormat format = stream.getFormat();
		if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
			format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(),
					format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2,
					format.getFrameRate(), true);
			stream = AudioSystem.getAudioInputStream(format, stream);
		}
		SourceDataLine.Info info = new DataLine.Info(SourceDataLine.class, stream.getFormat(),
				((int) stream.getFrameLength() * format.getFrameSize()));
		SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
		line.open(stream.getFormat());
		line.start();

		int numRead = 0;
		byte[] buf = new byte[line.getBufferSize()];
		while ((numRead = stream.read(buf, 0, buf.length)) >= 0) {
			int offset = 0;
			while (offset < numRead) {
				offset += line.write(buf, offset, numRead - offset);
			}
		}
		line.drain();
		line.stop();
	}

}
