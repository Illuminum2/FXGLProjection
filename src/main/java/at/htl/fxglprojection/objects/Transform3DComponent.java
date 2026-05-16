package at.htl.fxglprojection.objects;

import java.util.Arrays;
import java.util.List;

import com.almasb.fxgl.entity.component.Component;

import at.htl.fxglprojection.projection.Quaternion;
import at.htl.fxglprojection.projection.Vec3D;

public final class Transform3DComponent extends Component {
    private Vec3D position = new Vec3D();
    private Vec3D scale = new Vec3D(1, 1, 1);
    private Quaternion rotationQuat = new Quaternion();

    private final List<Boolean> changesSinceRead = Arrays.asList(true, true, true);

    public Vec3D getPosition() {
        changesSinceRead.set(0, false);
        return position;
    }
    public void setPosition(Vec3D position) {
        changesSinceRead.set(0, true);
        this.position = position;
    }

    public Vec3D getScale() {
        changesSinceRead.set(1, false);
        return scale;
    }
    public void setScale(Vec3D scale) {
        changesSinceRead.set(1, true);
        this.scale = scale;
    }

    public Quaternion getRotationQuat() {
        changesSinceRead.set(2, false);
        return rotationQuat;
    }
    public void setRotationQuat(Quaternion rotationDeg) {
        changesSinceRead.set(2, true);
        this.rotationQuat = rotationDeg;
    }

    public boolean changedSinceRead() {
        return changesSinceRead.contains(true);
    }

    public List<Boolean> getChanged() { return List.copyOf(changesSinceRead); }
}