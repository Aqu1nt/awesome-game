package ch.awesome.game.client.rendering.renderer

import ch.awesome.game.client.rendering.textures.GUITexture
import ch.awesome.game.client.rendering.ModelCreator
import ch.awesome.game.client.rendering.loading.TextureImageType
import ch.awesome.game.common.math.Matrix4f
import ch.awesome.game.common.math.inScreenHeight
import ch.awesome.game.common.math.inScreenWidth

class FontRenderer(private val renderer: GameRenderer) {

    val sheet by lazy { GUITexture(ModelCreator.loadGUITexture(renderer.gl, TextureImageType.FONT)) }
    val chars = HashMap<Char, CharInfo>()

    init {
        chars['A'] = CharInfo(0, 0, 12)
        chars['B'] = CharInfo(12, 0, 12)
        chars['C'] = CharInfo(24, 0, 12)
        chars['D'] = CharInfo(36, 0, 12)
        chars['E'] = CharInfo(48, 0, 12)
        chars['F'] = CharInfo(60, 0, 12)
        chars['G'] = CharInfo(72, 0, 12)
        chars['H'] = CharInfo(84, 0, 12)
        chars['I'] = CharInfo(96, 0, 12)
        chars['J'] = CharInfo(108, 0, 12)
        chars['K'] = CharInfo(120, 0, 12)
        chars['L'] = CharInfo(132, 0, 12)
        chars['M'] = CharInfo(144, 0, 12)
        chars['N'] = CharInfo(156, 0, 12)
        chars['O'] = CharInfo(168, 0, 12)
        chars['P'] = CharInfo(180, 0, 12)
        chars['Q'] = CharInfo(192, 0, 12)
        chars['R'] = CharInfo(204, 0, 12)
        chars['S'] = CharInfo(216, 0, 12)
        chars['T'] = CharInfo(228, 0, 12)
        chars['U'] = CharInfo(240, 0, 12)
        chars['V'] = CharInfo(252, 0, 12)
        chars['W'] = CharInfo(264, 0, 12)
        chars['X'] = CharInfo(276, 0, 12)
        chars['Y'] = CharInfo(288, 0, 12)
        chars['Z'] = CharInfo(300, 0, 12)
        chars['Ä'] = CharInfo(312, 0, 12)
        chars['Ü'] = CharInfo(324, 0, 12)
        chars['Ö'] = CharInfo(336, 0, 12)

        chars['a'] = CharInfo(0, 24, 12)
        chars['b'] = CharInfo(12, 24, 12)
        chars['c'] = CharInfo(24, 24, 12)
        chars['d'] = CharInfo(36, 24, 12)
        chars['e'] = CharInfo(48, 24, 12)
        chars['f'] = CharInfo(60, 24, 12)
        chars['g'] = CharInfo(72, 24, 12)
        chars['h'] = CharInfo(84, 24, 12)
        chars['i'] = CharInfo(96, 24, 12)
        chars['j'] = CharInfo(108, 24, 12)
        chars['k'] = CharInfo(120, 24, 12)
        chars['l'] = CharInfo(132, 24, 12)
        chars['m'] = CharInfo(144, 24, 12)
        chars['n'] = CharInfo(156, 24, 12)
        chars['o'] = CharInfo(168, 24, 12)
        chars['p'] = CharInfo(180, 24, 12)
        chars['q'] = CharInfo(192, 24, 12)
        chars['r'] = CharInfo(204, 24, 12)
        chars['s'] = CharInfo(216, 24, 12)
        chars['t'] = CharInfo(228, 24, 12)
        chars['u'] = CharInfo(240, 24, 12)
        chars['v'] = CharInfo(252, 24, 12)
        chars['w'] = CharInfo(264, 24, 12)
        chars['x'] = CharInfo(276, 24, 12)
        chars['y'] = CharInfo(288, 24, 12)
        chars['z'] = CharInfo(300, 24, 12)
        chars['ä'] = CharInfo(312, 24, 12)
        chars['ü'] = CharInfo(324, 24, 12)
        chars['ö'] = CharInfo(336, 24, 12)

        chars['0'] = CharInfo(0, 48, 12)
        chars['1'] = CharInfo(12, 48, 12)
        chars['2'] = CharInfo(24, 48, 12)
        chars['3'] = CharInfo(36, 48, 12)
        chars['4'] = CharInfo(48, 48, 12)
        chars['5'] = CharInfo(60, 48, 12)
        chars['6'] = CharInfo(72, 48, 12)
        chars['7'] = CharInfo(84, 48, 12)
        chars['8'] = CharInfo(96, 48, 12)
        chars['9'] = CharInfo(108, 48, 12)

        chars['.'] = CharInfo(0, 72, 12)
        chars[','] = CharInfo(12, 72, 12)
        chars[':'] = CharInfo(24, 72, 12)
        chars[';'] = CharInfo(36, 72, 12)
        chars['!'] = CharInfo(48, 72, 12)
        chars['?'] = CharInfo(60, 72, 12)
        chars['\"'] = CharInfo(72, 72, 12)
        chars['\''] = CharInfo(84, 72, 12)
        chars['+'] = CharInfo(96, 72, 12)
        chars['-'] = CharInfo(108, 72, 12)
        chars['='] = CharInfo(120, 72, 12)
        chars['%'] = CharInfo(132, 72, 12)
        chars['*'] = CharInfo(144, 72, 12)
        chars['_'] = CharInfo(156, 72, 12)
        chars['|'] = CharInfo(168, 72, 12)
        chars['/'] = CharInfo(180, 72, 12)
        chars['\\'] = CharInfo(192, 72, 12)
        chars['('] = CharInfo(204, 72, 12)
        chars[')'] = CharInfo(216, 72, 12)
        chars['['] = CharInfo(228, 72, 12)
        chars[']'] = CharInfo(240, 72, 12)
        chars['{'] = CharInfo(252, 72, 12)
        chars['}'] = CharInfo(264, 72, 12)
        chars['<'] = CharInfo(276, 72, 12)
        chars['>'] = CharInfo(288, 72, 12)
        chars['¦'] = CharInfo(300, 72, 12)

        chars[' '] = CharInfo(0, 96, 12)
    }

    fun render(text: String, x: Float, y: Float) {
        for(i in 0 until text.length) {
            val info = chars[text[i]]!!

            var charX = x

            if(i != 0) {
                charX = x + inScreenWidth(i * (chars[text[i - 1]]!!.width * 4.0f), renderer.canvas.width)
            }

            val charWidth = inScreenWidth(info.width * 2.0f, renderer.canvas.width)
            val charHeight = inScreenHeight(48.0f, renderer.canvas.height)

            val mat = Matrix4f().identity()
            mat.translate(charX, y, 0.0f)
            mat.scale(charWidth, charHeight, 1.0f)

            val textureX = (1.0f / 512.0f) * info.x
            val textureY = (1.0f / 512.0f) * info.y
            val textureWidth = (1.0f / 512.0f) * info.width
            val textureHeight = (1.0f / 512.0f) * 27.0f

            renderer.renderGUI(sheet, mat, textureX, textureY, textureWidth, textureHeight)
        }
    }

    class CharInfo(val x: Int, val y: Int, val width: Int)
}