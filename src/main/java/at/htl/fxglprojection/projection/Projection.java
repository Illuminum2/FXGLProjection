package at.htl.fxglprojection.projection;

import org.jetbrains.annotations.Nullable;

public class Projection {
    public Camera3DProjection camera;
    private double[][] R;

    public Projection() {
        this.camera = new Camera3DProjection();
        this.R = new double[][] {{ 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 }};
    }

    @Nullable
    public Vec2D projectPoint(Vec3D p) {
        double P1x = p.x - this.camera.getX();
        double P1y = p.y - this.camera.getY();
        double P1z = p.z - this.camera.getZ();

        double[][] R_inv = MathHelper.matrixTranspose(R);

        double P2x = (R_inv[0][0]*P1x) + (R_inv[0][1]*P1y) + (R_inv[0][2]*P1z);
        double P2y = (R_inv[1][0]*P1x) + (R_inv[1][1]*P1y) + (R_inv[1][2]*P1z);
        double P2z = (R_inv[2][0]*P1x) + (R_inv[2][1]*P1y) + (R_inv[2][2]*P1z);

        if (P2z > 0) {
            return new Vec2D(
                    this.camera.getFocalLength() * P2x / P2z,
                    this.camera.getFocalLength() * P2y / P2z
            );
        }
        else {
            return null;
        }
    }
}
