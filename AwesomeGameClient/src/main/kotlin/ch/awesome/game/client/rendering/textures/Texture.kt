package ch.awesome.game.client.rendering.textures

import org.khronos.webgl.WebGLTexture

class Texture (val modelTexture: WebGLTexture,
               val reflectivity: Float = 1f,
               val shineDamper: Float = 20f,
               val lightMap: WebGLTexture?= null) {
}