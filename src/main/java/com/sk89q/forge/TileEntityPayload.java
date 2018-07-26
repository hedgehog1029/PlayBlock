package com.sk89q.forge;

import com.skcraft.playblock.projector.TileEntityProjector;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

/**
 * A payload that is targeted specifically to a tile entity.
 */
public class TileEntityPayload implements Payload {

    private int x;
    private int y;
    private int z;
    private Payload payload;

    public TileEntityPayload() {

    }

    public TileEntityPayload(TileEntityProjector tile, Payload payload) {
        x = tile.getPos().getX();
        y = tile.getPos().getY();
        z = tile.getPos().getZ();
        this.payload = payload;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public void read(ByteBufInputStream in) throws IOException {
        x = in.readInt();
        y = in.readInt();
        z = in.readInt();
        if (payload != null) {
            payload.read(in);
        }
    }

    @Override
    public void write(ByteBufOutputStream out) throws IOException {
        out.writeInt(x);
        out.writeInt(y);
        out.writeInt(z);
        if (payload != null) {
            payload.write(out);
        }
    }

}
