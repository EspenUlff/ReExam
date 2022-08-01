package com.dtu.common.model.fileaccess;

import com.dtu.common.model.FieldAction;
import com.dtu.common.model.Heading;
import com.dtu.common.model.fieldTypes.Checkpoint;
import com.dtu.common.model.fieldTypes.ConveyorBelt;
import com.dtu.common.model.fieldTypes.RotateGear;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class FieldActionTypeAdapter extends TypeAdapter<FieldAction> {
    @Override
    public void write(JsonWriter out, FieldAction value) throws IOException {
        out.beginObject();
        out.name("type");
        if (value instanceof Checkpoint checkpoint) {
            out.value("checkPoint");
            out.name("checkPointNumber");
            out.value(checkpoint.getCheckpointNumber());
        }
        if (value instanceof ConveyorBelt conveyorBelt) {
            out.value("conveyorBelt");
            out.name("heading");
            out.value(conveyorBelt.getHeading().toString());
        }
        if (value instanceof RotateGear rotateGear) {
            out.value("rotateGear");
        }
        out.endObject();
    }


    @Override
    public FieldAction read(JsonReader in) throws IOException {
        in.beginObject();
        FieldAction action = null;

        if ("type".equals(in.nextName())) {
            String type = in.nextString();
            if ("checkPoint".equals(type)) {
                in.nextName();
                int checkPointNumber = in.nextInt();
                action = new Checkpoint(checkPointNumber);
            }
            if ("conveyorBelt".equals(type)) {
                in.nextName();
                String heading = in.nextString();
                action = new ConveyorBelt(Heading.valueOf(heading));
            }
            if ("rotateGear".equals(type)) {
                action = new RotateGear();
            }
        }
        in.endObject();
        return action;
    }
}
