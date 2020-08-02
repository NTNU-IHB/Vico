package no.ntnu.ihb.vico.input


enum class KeyStroke {

    KEY_SHIFT_LEFT,
    KEY_SHIFT_RIGHT,
    KEY_CTRL_LEFT,
    KEY_CTRL_RIGHT,
    KEY_TAB_RIGHT,
    KEY_ENTER,
    KEY_BACKSPACE,
    KEY_ALT,
    KEY_SPACE,
    KEY_COMMA,
    KEY_DOT,
    KEY_MINUS,
    KEY_PLUS,

    ARROW_LEFT,
    ARROW_RIGHT,
    ARROW_UP,
    ARROW_DOWN,

    KEY_1,
    KEY_2,
    KEY_3,
    KEY_4,
    KEY_5,
    KEY_6,
    KEY_7,
    KEY_8,
    KEY_9,
    KEY_0,

    NUMPAD_1,
    NUMPAD_2,
    NUMPAD_3,
    NUMPAD_4,
    NUMPAD_5,
    NUMPAD_6,
    NUMPAD_7,
    NUMPAD_8,
    NUMPAD_9,
    NUMPAD_0,

    KEY_A,
    KEY_B,
    KEY_C,
    KEY_D,
    KEY_E,
    KEY_F,
    KEY_G,
    KEY_H,
    KEY_I,
    KEY_J,
    KEY_K,
    KEY_L,
    KEY_M,
    KEY_N,
    KEY_O,
    KEY_P,
    KEY_Q,
    KEY_R,
    KEY_S,
    KEY_T,
    KEY_U,
    KEY_V,
    KEY_W,
    KEY_X,
    KEY_Y,
    KEY_Z,

    KEY_F1,
    KEY_F2,
    KEY_F3,
    KEY_F4,
    KEY_F5,
    KEY_F6,
    KEY_F7,
    KEY_F8,
    KEY_F9,
    KEY_F10,
    KEY_F11,
    KEY_F12,

    UNDEFINED;

    companion object {

        fun fromString(keyName: String): KeyStroke? {
            for (key in values()) {
                if (key.name == keyName.toUpperCase()) {
                    return key
                }
            }
            return null
        }

    }
}
