Ext.define("public.BaseWin",{
    extend: 'Ext.window.Window',
    alias: 'widget.baseWin',
    modal: true,
//    width: 720,
    maxHeight:700,
    autoScroll:false,
//    overflowY:'auto',
    layout: 'anchor',
    requires: [
        'public.BaseComboBoxTree',
        'public.BaseForm'
    ],
    listeners: {
        move: function (win, x, y, eOpts) {
            var vw = Ext.Element.getViewWidth(),
                vh = Ext.Element.getViewHeight(),
                ww = win.getWidth(),
                wh = win.getHeight();
            if (vw > ww) {
                if (x < 0)
                    win.setPosition(0, y);
                if (x > vw - ww)
                    win.setPosition(vw - ww, y);
            } else if (x != 0) {
                win.setPosition(0, y);
            }
            if (vh > wh) {
                if (y < 0)
                    win.setPosition(x, 0);
                if (y > vh - 37)
                    win.setPosition(x, vh - 37);
            } else if (y != 0) {
                win.setPosition(x, 0);
            }
        }
    }
});
