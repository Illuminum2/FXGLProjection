package at.htl.fxglprojection.objects;

import com.almasb.fxgl.entity.component.Component;

import at.htl.fxglprojection.projection.Quaternion;
import at.htl.fxglprojection.projection.Vec3D;

public final class Transform3DComponent extends Component {
    private Vec3D position = new Vec3D();
    private Quaternion rotationQuat = new Quaternion();
    private Vec3D scale = new Vec3D(1, 1, 1);

    private final boolean[] changesSinceRead = new boolean[]{true, true, true};

    public Vec3D getPosition() {
        changesSinceRead[0] = false;
        return position;
    }
    public void setPosition(Vec3D position) {
        changesSinceRead[0] = true;
        this.position = position;
    }

    public Quaternion getRotationQuat() {
        changesSinceRead[1] = false;
        return rotationQuat;
    }
    public void setRotationQuat(Quaternion rotationDeg) {
        changesSinceRead[1] = true;
        this.rotationQuat = rotationDeg;
    }

    public Vec3D getScale() {
        changesSinceRead[2] = false;
        return scale;
    }
    public void setScale(Vec3D scale) {
        changesSinceRead[2] = true;
        this.scale = scale;
    }

    public boolean changedSinceRead() {
        return changesSinceRead[0] || changesSinceRead[1] || changesSinceRead[2];
    }
}