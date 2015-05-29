package org.securipi.securipi;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Classe de conversion d'un flux vidéo en images
 */
public class MjpegInputStream extends DataInputStream {
    private final static int HEADER_MAX_LENGTH = 100;
    private final static int FRAME_MAX_LENGTH = 40000 + HEADER_MAX_LENGTH;
    private final byte[] SOI_MARKER = {(byte) 0xFF, (byte) 0xD8};
    private final byte[] EOF_MARKER = {(byte) 0xFF, (byte) 0xD9};
    private final String CONTENT_LENGTH = "Content-Length";
    private int mContentLength = -1;

    /**
     * Déclare le nombre maximum de frame
     * @param in <InputStream>
     */
    public MjpegInputStream(InputStream in) {
        super(new BufferedInputStream(in, FRAME_MAX_LENGTH));
    }

    /**
     * Lis le flux video Mjpeg depuis une URL
     * @param url <String>
     * @return null
     */
    public static MjpegInputStream read(String url) {
        HttpResponse res;
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            res = httpclient.execute(new HttpGet(URI.create(url)));
            return new MjpegInputStream(res.getEntity().getContent());
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * Retourne la fin de la séquence
     * @param in <DataInputStream>
     * @param sequence <byte[]>
     * @return endSequence <int>
     * @throws IOException
     */
    private int getEndOfSequence(DataInputStream in, byte[] sequence) throws IOException {
        int seqIndex = 0;
        byte c;
        for (int i = 0; i < FRAME_MAX_LENGTH; i++) {
            c = (byte) in.readUnsignedByte();
            if (c == sequence[seqIndex]) {
                seqIndex++;
                if (seqIndex == sequence.length) return i + 1;
            } else seqIndex = 0;
        }
        return -1;
    }

    /**
     * Retourne le début de la séquence
     * @param in <DataInputStream>
     * @param sequence <byte[]>
     * @return startSequence <int>
     * @throws IOException
     */
    private int getStartOfSequence(DataInputStream in, byte[] sequence) throws IOException {
        int end = getEndOfSequence(in, sequence);
        return (end < 0) ? (-1) : (end - sequence.length);
    }

    /**
     * Parcours le contenu
     * @param headerBytes <byte[]>
     * @return property <int>
     * @throws IOException
     * @throws NumberFormatException
     */
    private int parseContentLength(byte[] headerBytes) throws IOException, NumberFormatException {
        ByteArrayInputStream headerIn = new ByteArrayInputStream(headerBytes);
        Properties props = new Properties();
        props.load(headerIn);
        return Integer.parseInt(props.getProperty(CONTENT_LENGTH));
    }

    /**
     * Retourne une image depuis la frame
     * @return image <Bitmap>
     * @throws IOException
     */
    public Bitmap readMjpegFrame() throws IOException {
        mark(FRAME_MAX_LENGTH);
        int headerLen = getStartOfSequence(this, SOI_MARKER);
        reset();
        byte[] header = new byte[headerLen];
        readFully(header);
        try {
            mContentLength = parseContentLength(header);
        } catch (NumberFormatException nfe) {
            mContentLength = getEndOfSequence(this, EOF_MARKER);
        }
        reset();
        byte[] frameData = new byte[mContentLength];
        skipBytes(headerLen);
        readFully(frameData);
        return BitmapFactory.decodeStream(new ByteArrayInputStream(frameData));
    }
}