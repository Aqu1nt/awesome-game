package ch.awesome.game.client.rendering.postprocessing

import ch.awesome.game.client.rendering.shader.postprocessing.bloom.BloomCombineShader
import ch.awesome.game.client.lib.WebGL2RenderingContext
import org.khronos.webgl.WebGLRenderingContext
import org.khronos.webgl.WebGLTexture

class BloomCombiner(private val gl: WebGL2RenderingContext, private val displayWidth: Int, private val displayHeight: Int) {

    val renderer = PostProcessingRenderer(gl)
    val shader = BloomCombineShader(gl)

    init {
        shader.start()
        shader.findAllUniformLocations()
        shader.sceneTexture.load(gl, 0)
        shader.brightTexture.load(gl, 1)
        shader.stop()
    }

    fun render(sceneTexture: WebGLTexture, brightTexture: WebGLTexture) {
        shader.start()

        gl.activeTexture(WebGLRenderingContext.TEXTURE0)
        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, sceneTexture)

        gl.activeTexture(WebGLRenderingContext.TEXTURE1)
        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, brightTexture)

        renderer.render(displayWidth, displayHeight)

        shader.stop()
    }
}