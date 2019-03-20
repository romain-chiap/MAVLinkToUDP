package coriolis;

import jssc.SerialPort;
import jssc.SerialPortException;
import me.drton.jmavlib.mavlink.MAVLinkMessage;
import me.drton.jmavlib.mavlink.MAVLinkSchema;
import me.drton.jmavlib.mavlink.MAVLinkStream;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
//import java.util.List;
//import java.util.Arrays;
//import java.util.ArrayList;

/**
 * User: ton Date: 28.11.13 Time: 23:30
 */
public class SerialMAVLinkPort {
    private MAVLinkSchema schema = null;
    private SerialPort serialPort = null;
    private ByteChannel channel = null;
    private MAVLinkStream stream = null;
    private boolean debug = false;

    // connection information
    String portName;
    int baudRate;
    int dataBits;
    int stopBits;
    int parity;

    public SerialMAVLinkPort(MAVLinkSchema schema) {
        this.schema = schema;
    }

    public void setup(String portName, int baudRate, int dataBits, int stopBits, int parity) {
        this.portName = portName;
        this.baudRate = baudRate;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.parity = parity;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void open() throws IOException {
        serialPort = new SerialPort(portName);
        try {
            serialPort.openPort();
            serialPort.setParams(baudRate, dataBits, stopBits, parity);
        } catch (SerialPortException e) {
            serialPort = null;
            throw new IOException(e);
        }
        channel = new ByteChannel() {
            @Override
            public int read(ByteBuffer buffer) throws IOException {
                try {
                    int available = serialPort.getInputBufferBytesCount();
                    if (available <= 0) {
                        return 0;
                    }

                    byte[] b = serialPort.readBytes(Math.min(available, buffer.remaining()));
                    if (b != null) {
                        buffer.put(b);
                        return b.length;
                    }

                    return 0;

                } catch (SerialPortException e) {
                    throw new IOException(e);
                }
            }

            @Override
            public int write(ByteBuffer buffer) throws IOException {
                try {
                    byte[] b = new byte[buffer.remaining()];
                    buffer.get(b);
                    return serialPort.writeBytes(b) ? b.length : 0;
                } catch (SerialPortException e) {
                    throw new IOException(e);
                }
            }

            @Override
            public boolean isOpen() {
                return serialPort.isOpened();
            }

            @Override
            public void close() throws IOException {
                try {
                    serialPort.closePort();
                } catch (SerialPortException e) {
                    throw new IOException(e);
                }
            }
        };
        stream = new MAVLinkStream(schema, channel);
        stream.setDebug(debug);
    }

    public void close() throws IOException {
        if (serialPort == null) {
            return;
        }

        try {
            serialPort.closePort();
        } catch (SerialPortException e) {
            throw new IOException(e);
        }
        serialPort = null;
        stream = null;
    }

    public boolean isOpened() {
        return serialPort != null && serialPort.isOpened();
    }

    public void handleMessage(MAVLinkMessage msg) {
        if (isOpened() && stream != null) {
            try {
                stream.write(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public MAVLinkMessage update(String msgName) {
        MAVLinkMessage msg=null;
        while (isOpened() && stream != null) {
            try {
                msg = stream.read();
                if (msg == null) {
                    return null;
                }
                if (msgName.equals(msg.getMsgName())) {
                	return msg;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return msg;
    }

    public void sendRaw(byte[] data) throws IOException {
        try {
            serialPort.writeBytes(data);
        } catch (SerialPortException e) {
            throw new IOException(e);
        }
    }
}