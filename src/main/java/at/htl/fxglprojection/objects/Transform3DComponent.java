package at.htl.fxglprojection.objects;

import com.almasb.fxgl.entity.component.Component;

import at.htl.fxglprojection.projection.Quaternion;
import at.htl.fxglprojection.projection.Vec3D;

public final class Transform3DComponent extends Component {
    private Vec3D position = new Vec3D();
    private Quaternion rotationQuat = new Quaternion();
    private Vec3D scale = new Vec3D(1, 1, 1);

    public Vec3D getPosition() {
        return position;
    }
    public void setPosition(Vec3D position) {
        this.position = position;
    }

    public Quaternion getRotationQuat() {
        return rotationQuat;
    }
    public void setRotationQuat(Quaternion rotationDeg) {
        this.rotationQuat = rotationDeg;
    }

    public Vec3D getScale() {
        return scale;
    }
    public void setScale(Vec3D scale) {
        this.scale = scale;
    }
}