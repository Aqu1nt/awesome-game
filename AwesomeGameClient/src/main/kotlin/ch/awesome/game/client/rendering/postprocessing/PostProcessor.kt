package ch.awesome.game.client.rendering.postprocessing

import ch.awesome.game.client.rendering.ModelCreator
import ch.awesome.game.client.lib.WebGL2RenderingContext
import org.khronos.webgl.WebGLRenderingContext

class PostProcessor(val gl: WebGL2RenderingContext, private val displayWidth: Int, private val displayHeight: Int) {

    val model = ModelCreator.loadModel(gl, arrayOf(-1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f, -1.0f), 2)

    private val startFBO = FrameBufferObject(gl, displayWidth, displayHeight)

    private val brightFilter = BloomBrightFilter(gl, displayWidth, displayHeight, displayWidth / 4, displayHeight / 4)
    private val horizontalBlur = HorizontalBlur(gl, displayWidth, displayHeight, displayWidth / 8, displayHeight / 8)
    private val verticalBlur = VerticalBlur(gl, displayWidth, displayHeight, displayWidth / 8, displayHeight / 8)
    private val combiner = BloomCombiner(gl, displayWidth, displayHeight)

    fun beforeRendering() {
        startFBO.bindToDraw()
    }

    fun prepare() {
        gl.bindVertexArray(model.vao)
        gl.enableVertexAttribArray(0)
        gl.disable(WebGLRenderingContext.DEPTH_TEST)
    }

    fun process() {
        startFBO.unbind(displayWidth, displayHeight)

        prepare()
        brightFilter.render(startFBO.colorTexture!!)
        horizontalBlur.render(brightFilter.renderer.getOutputTexture())
        verticalBlur.render(horizontalBlur.renderer.getOutputTexture())
        combiner.render(startFBO.colorTexture!!, verticalBlur.renderer.getOutputTexture())
        end()
    }

    fun end() {
        gl.enable(WebGLRenderingContext.DEPTH_TEST)
        gl.disableVertexAttribArray(0)
        gl.bindVertexArray(null)
    }
}