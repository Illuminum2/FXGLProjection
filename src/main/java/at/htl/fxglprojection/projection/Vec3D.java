package at.htl.fxglprojection.projection;

public class Vec3D {
    public double x = 0;
    public double y = 0;
    public double z = 0;

    public Vec3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3D() { }

    public Vec3D add(Vec3D o) {
        return new Vec3D(this.x+o.x, this.y+o.y, this.z+o.z);
    }

    public Vec3D add(double o) {
        return new Vec3D(this.x+o, this.y+o, this.z+o);
    }

    public Vec3D subtract(Vec3D o) {
        return new Vec3D(this.x-o.x, this.y-o.y, this.z-o.z);
    }

    public Vec3D subtract(double o) {
        return new Vec3D(this.x-o, this.y-o, this.z-o);
    }

    public Vec3D multiply(Vec3D o) {
        return new Vec3D(this.x*o.x, this.y*o.y, this.z*o.z);
    }

    public Vec3D multiply(double o) {
        return new Vec3D(this.x*o, this.y*o, this.z*o);
    }

    public Vec3D divide(Vec3D o) {
        return new Vec3D(this.x/o.x, this.y/o.y, this.z/o.z);
    }

    public Vec3D divide(double o) {
        return new Vec3D(this.x/o, this.y/o, this.z/o);
    }

    public double magnitude() {
        return Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
    }

    public Vec3D normalized() {
        double magnitude = magnitude();

        if (magnitude == 0)
            return new Vec3D(0, 0, 0);
        return divide(magnitude);
    }

    public double dot(Vec3D o) {
        return this.x*o.x + this.y*o.y + this.z*o.z;
    }
}
