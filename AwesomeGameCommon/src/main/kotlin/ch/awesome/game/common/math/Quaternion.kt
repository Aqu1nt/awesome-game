package ch.awesome.game.common.math

import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin
import kotlin.math.sqrt

class Quaternion(var x: Float = 0f,
                 var y: Float = 0f,
                 var z: Float = 0f,
                 var w: Float = 1f) {

    fun norm(): Float {
        return w * w + x * x + y * y + z * z
    }

    fun multLocal(v: Vector3f): Vector3f {
        val tempX: Float = (w * w * v.x + 2f * y * w * v.z - 2f * z * w * v.y + x * x * v.x
                            + 2f * y * x * v.y + 2f * z * x * v.z) - z * z * v.x - y * y * v.x
        val tempY: Float = 2f * x * y * v.x + y * y * v.y + 2f * z * y * v.z + (2f * w * z
                                                                                * v.x) - z * z * v.y + w * w * v.y - 2f * x * w * v.z - (x * x
                                                                                                                                         * v.y)
        v.z = (2f * x * z * v.x + 2f * y * z * v.y + z * z * v.z - 2f * w * y * v.x
               - y * y * v.z) + 2f * w * x * v.y - x * x * v.z + w * w * v.z
        v.x = tempX
        v.y = tempY
        return v
    }

    fun fromAngles(vec: IVector3f): Quaternion {
        return fromAngles(vec.x, vec.y, vec.z)
    }

    fun fromAngles(xAngle: Float, yAngle: Float, zAngle: Float): Quaternion {
        val sinY: Float
        val sinZ: Float
        val sinX: Float
        val cosY: Float
        val cosZ: Float
        val cosX: Float
        var angle: Float = toRadians(zAngle) * 0.5f
        sinZ = sin(angle)
        cosZ = cos(angle)
        angle = toRadians(yAngle) * 0.5f
        sinY = sin(angle)
        cosY = cos(angle)
        angle = toRadians(xAngle) * 0.5f
        sinX = sin(angle)
        cosX = cos(angle)

        // variables used to reduce multiplication calls.
        val cosYXcosZ = cosY * cosZ
        val sinYXsinZ = sinY * sinZ
        val cosYXsinZ = cosY * sinZ
        val sinYXcosZ = sinY * cosZ

        w = cosYXcosZ * cosX - sinYXsinZ * sinX
        x = cosYXcosZ * sinX + sinYXsinZ * cosX
        y = sinYXcosZ * cosX + cosYXsinZ * sinX
        z = cosYXsinZ * cosX - sinYXcosZ * sinX

        normalizeLocal()
        return this
    }

    fun fromMatrix(mat: Matrix4f): Quaternion {
        var s = 0.0f;
		var tr = mat.m00 + mat.m11 + mat.m22

		if (tr >= 0.0) {
			s = sqrt(tr + 1.0).toFloat()
			w = s * 0.5f;
			s = 0.5f / s;
			x = (mat.m21 - mat.m12) * s;
			y = (mat.m02 - mat.m20) * s;
			z = (mat.m10 - mat.m01) * s;
		} else {
			val max = max(max(mat.m00, mat.m11), mat.m22);
			if (max == mat.m00) {
				s = sqrt(mat.m00 - (mat.m11 + mat.m22) + 1.0).toFloat()
				x = s * 0.5f
				s = 0.5f / s
				y = (mat.m01 + mat.m10) * s;
				z = (mat.m20 + mat.m02) * s;
				w = (mat.m21 - mat.m12) * s;
			} else if (max == mat.m11) {
				s = sqrt(mat.m11 - (mat.m22 + mat.m00) + 1.0).toFloat()
				y = s * 0.5f;
				s = 0.5f / s;
				z = (mat.m12 + mat.m21) * s;
				x = (mat.m01 + mat.m10) * s;
				w = (mat.m02 - mat.m20) * s;
			} else {
				s = sqrt(mat.m22 - (mat.m00 + mat.m11) + 1.0).toFloat()
				z = s * 0.5f;
				s = 0.5f / s;
				x = (mat.m20 + mat.m02) * s;
				y = (mat.m12 + mat.m21) * s;
				w = (mat.m10 - mat.m01) * s;
			}
		}

        return this
    }

    fun toRotationMatrix(result: Matrix4f): Matrix4f {
        val originalScale = Vector3f()

        result.toScaleVector(originalScale)
        result.setScale(1f, 1f, 1f)
        val norm = norm()
        // we explicitly test norm against one here, saving a division
        // at the cost of a test and branch.  Is it worth it?
        val s = if (norm == 1f) 2f else if (norm > 0f) 2f / norm else 0f

        // compute xs/ys/zs first to save 6 multiplications, since xs/ys/zs
        // will be used 2-4 times each.
        val xs = x * s
        val ys = y * s
        val zs = z * s
        val xx = x * xs
        val xy = x * ys
        val xz = x * zs
        val xw = w * xs
        val yy = y * ys
        val yz = y * zs
        val yw = w * ys
        val zz = z * zs
        val zw = w * zs

        // using s=2/norm (instead of 1/norm) saves 9 multiplications by 2 here
        result.m00 = 1 - (yy + zz)
        result.m01 = xy - zw
        result.m02 = xz + yw
        result.m10 = xy + zw
        result.m11 = 1 - (xx + zz)
        result.m12 = yz - xw
        result.m20 = xz - yw
        result.m21 = yz + xw
        result.m22 = 1 - (xx + yy)

        result.setScale(originalScale)

        return result
    }

    fun mult(q: Quaternion, res: Quaternion = Quaternion()): Quaternion {
        val qw = q.w
        val qx = q.x
        val qy = q.y
        val qz = q.z
        res.x = x * qw + y * qz - z * qy + w * qx
        res.y = -x * qz + y * qw + z * qx + w * qy
        res.z = x * qy - y * qx + z * qw + w * qz
        res.w = -x * qx - y * qy - z * qz + w * qw
        return res
    }

    fun normalizeLocal(): Quaternion {
        val n = invSqrt(norm())
        x *= n
        y *= n
        z *= n
        w *= n
        return this
    }

    fun inverse(): Quaternion {
        val norm = norm()
        if (norm > 0.0) {
            val invNorm = 1.0f / norm
            return Quaternion(-x * invNorm, -y * invNorm, -z * invNorm, w * invNorm)
        }
        throw IllegalStateException()
    }

    fun inverseLocal(): Quaternion {
        val norm = norm()
        if (norm > 0.0) {
            val invNorm = 1.0f / norm
            x = -x * invNorm
            y = -y * invNorm
            z = -z * invNorm
            w = w * invNorm
            return this
        }
        throw IllegalStateException()
    }

    fun interpolate(end: Quaternion, progression: Float): Quaternion {
        val result = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)

        val dot = w * end.w + x * end.x + y * end.y + z * end.z
        val oneMinusProgression = 1.0f - progression

        if (dot < 0) {
            result.w = oneMinusProgression * w + progression * -end.w
            result.x = oneMinusProgression * x + progression * -end.x
            result.y = oneMinusProgression * y + progression * -end.y
            result.z = oneMinusProgression * z + progression * -end.z
        } else {
            result.w = oneMinusProgression * w + progression * end.w
            result.x = oneMinusProgression * x + progression * end.x
            result.y = oneMinusProgression * y + progression * end.y
            result.z = oneMinusProgression * z + progression * end.z
        }

		result.norm()

        return result
    }
}