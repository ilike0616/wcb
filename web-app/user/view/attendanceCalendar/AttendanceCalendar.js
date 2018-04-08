/**
 * Created by guozhen on 2015/4/22.
 */
Ext.define('user.view.attendanceCalendar.AttendanceCalendar', {
    extend: 'Ext.picker.Date',
    //extend: 'Ext.panel.Panel',
    alias: 'widget.attendanceCalendar',
    title: '考勤日历',
    layout: 'fit',
    width: 800,
    attendanceCalendar:null,
    renderTpl: [
        '<div id="{id}-innerEl" role="grid">',
        '<div role="presentation" class="{baseCls}-header">',
        // the href attribute is required for the :hover selector to work in IE6/7/quirks
        '<a id="{id}-prevEl" class="{baseCls}-prev {baseCls}-arrow" href="#" role="button" title="{prevText}" hidefocus="on" ></a>',
        '<div class="{baseCls}-month" id="{id}-middleBtnEl">{%this.renderMonthBtn(values, out)%}</div>',
        // the href attribute is required for the :hover selector to work in IE6/7/quirks
        '<a id="{id}-nextEl" class="{baseCls}-next {baseCls}-arrow" href="#" role="button" title="{nextText}" hidefocus="on" ></a>',
        '</div>',
        '<table id="{id}-eventEl" class="{baseCls}-inner" cellspacing="0" role="grid">',
        '<thead role="presentation"><tr role="row">',
        '<tpl for="dayNames">',
        '<th role="columnheader" class="{parent.baseCls}-column-header" title="{.}" style="text-align: center">',
        '<div class="{parent.baseCls}-column-header-inner">{.:this.firstInitial}</div>',
        '</th>',
        '</tpl>',
        '</tr></thead>',
        '<tbody role="presentation"><tr role="row">',
        '<tpl for="days">',
        '{#:this.isEndOfWeek}',
        '<td role="gridcell" id="{[Ext.id()]}" style="height: 50px;text-align: center;">',
        // the href attribute is required for the :hover selector to work in IE6/7/quirks
        '<a role="presentation" hidefocus="on" class="{parent.baseCls}-date" href="#"></a>',
        '<label style="text-align: left;font-size: 8px;"></label>',
        '</td>',
        '</tpl>',
        '</tr></tbody>',
        '</table>',
        '<tpl if="showToday">',
        '<div id="{id}-footerEl" role="presentation" class="{baseCls}-footer" hidden="hidden">{%this.renderTodayBtn(values, out)%}</div>',
        '</tpl>',
        '</div>',
        {
            firstInitial: function (value) {
                return Ext.picker.Date.prototype.getDayInitial(value);
            },
            isEndOfWeek: function (value) {
                value--;
                var end = value % 7 === 0 && value !== 0;
                return end ? '</tr><tr role="row">' : '';
            },
            renderTodayBtn: function (values, out) {
                Ext.DomHelper.generateMarkup(values.$comp.todayBtn.getRenderTree(), out);
            },
            renderMonthBtn: function (values, out) {
                Ext.DomHelper.generateMarkup(values.$comp.monthBtn.getRenderTree(), out);
            }
        }
    ],
    initComponent: function () {
        var me = this;
        Ext.applyIf(me,{
        })
        me.callParent(arguments);
    },
    beforeRender: function () {
        var me = this,
            days = new Array(me.numDays);
        me.callParent();
        Ext.applyIf(me, {
            renderData: {}
        });

        Ext.apply(me.renderData, {
            dayNames: me.dayNames,
            days: days
        });
    },
    getSpecialDay: function () {
        var specialDays = new Ext.util.HashMap();
        Ext.Ajax.request({
            url: 'attendanceCalendar/getSpecialDay',
            params:{
                attendanceCalendar:this.attendanceCalendar
            },
            method: 'POST',
            timeout: 4000,
            async: false,
            success: function (response, opts) {
                var view = Ext.JSON.decode(response.responseText);
                Ext.Array.each(view.specialDays, function (v) {
                    specialDays.add(v.workDate, v);
                })
            }
        });
        return specialDays;
    },
    /**
     * Update the selected cell
     * @private
     * @param {Date} date The new date
     */
    selectedUpdate: function(date){
        var me        = this,
            t         = date.getTime(),
            cells     = me.cells,
            cls       = me.selectedCls,
            cellItems = cells.elements,
            c,
            cLen      = cellItems.length,
            cell;

        cells.removeCls(cls);

        for (c = 0; c < cLen; c++) {
            cell = Ext.fly(cellItems[c]);

            var cellDomFirstChild = cell.dom.firstChild;
            if (cellDomFirstChild.dateValue == t) {
                var kind = cellDomFirstChild.kind;
                var remark = cellDomFirstChild.remark;
                me.fireEvent('highlightitem', me, cell);
                cell.addCls(cls);
                if(me.isVisible() && !me.doCancelFocus){
                    Ext.fly(cellDomFirstChild).focus(50);
                }

                var workDayInfo = me.up('attendanceCalendarMain').down('workDayInfo');
                workDayInfo.down('radiogroup').setValue(kind);
                workDayInfo.down('textarea').setValue(remark);
                if(kind != null && kind != ""){
                    workDayInfo.down('button#attendance_calendar_delete').setVisible(true);
                }else{
                    workDayInfo.down('button#attendance_calendar_delete').setVisible(false);
                }
                break;
            }
        }
    },
    fullUpdate: function (date) {
        this.setLoading('加载中...');
        if (Ext.typeOf(date) == 'undefined') date = new Date();
        var me = this,
            cells = me.cells.elements,
            textNodes = me.textNodes,
            disabledCls = me.disabledCellCls,
            eDate = Ext.Date,
            i = 0,
            extraDays = 0,
            visible = me.isVisible(),
            newDate = +eDate.clearTime(date, true),
            today = +eDate.clearTime(new Date()),
            min = me.minDate ? eDate.clearTime(me.minDate, true) : Number.NEGATIVE_INFINITY,
            max = me.maxDate ? eDate.clearTime(me.maxDate, true) : Number.POSITIVE_INFINITY,
            ddMatch = me.disabledDatesRE,
            ddText = me.disabledDatesText,
            ddays = me.disabledDays ? me.disabledDays.join('') : false,
            ddaysText = me.disabledDaysText,
            format = me.format,
            days = eDate.getDaysInMonth(date),
            firstOfMonth = eDate.getFirstDateOfMonth(date),
            startingPos = firstOfMonth.getDay() - me.startDay,
            previousMonth = eDate.add(date, eDate.MONTH, -1),
            longDayFormat = me.longDayFormat,
            prevStart,
            current,
            disableToday,
            tempDate,
            setCellClass,
            html,
            cls,
            formatValue,
            value;

        if (startingPos < 0) {
            startingPos += 7;
        }

        days += startingPos;
        prevStart = eDate.getDaysInMonth(previousMonth) - startingPos;
        current = new Date(previousMonth.getFullYear(), previousMonth.getMonth(), prevStart, me.initHour);

        if (me.showToday) {
            tempDate = eDate.clearTime(new Date());
            disableToday = (tempDate < min || tempDate > max ||
            (ddMatch && format && ddMatch.test(eDate.dateFormat(tempDate, format))) ||
            (ddays && ddays.indexOf(tempDate.getDay()) != -1));

            if (!me.disabled) {
                me.todayBtn.setDisabled(disableToday);
                me.todayKeyListener.setDisabled(disableToday);
            }
        }

        setCellClass = function (cell, cls) {
            value = +eDate.clearTime(current, true);
            cell.title = eDate.format(current, longDayFormat);
            // store dateValue number as an expando
            cell.firstChild.dateValue = value;
            if (value == today) {
                cls += ' ' + me.todayCls;
                cell.title = me.todayText;

                // Extra element for ARIA purposes
                me.todayElSpan = Ext.DomHelper.append(cell.firstChild, {
                    tag: 'span',
                    cls: Ext.baseCSSPrefix + 'hide-clip',
                    html: me.todayText
                }, true);
            }
            if (value == newDate) {
                cls += ' ' + me.selectedCls;
                me.fireEvent('highlightitem', me, cell);
                if (visible && me.floating) {
                    Ext.fly(cell.firstChild).focus(50);
                }
            }

            if (value < min) {
                cls += ' ' + disabledCls;
                cell.title = me.minText;
            }
            else if (value > max) {
                cls += ' ' + disabledCls;
                cell.title = me.maxText;
            }
            else if (ddays && ddays.indexOf(current.getDay()) !== -1) {
                cell.title = ddaysText;
                cls += ' ' + disabledCls;
            }
            else if (ddMatch && format) {
                formatValue = eDate.dateFormat(current, format);
                if (ddMatch.test(formatValue)) {
                    cell.title = ddText.replace('%0', formatValue);
                    cls += ' ' + disabledCls;
                }
            }
            cell.className = cls + ' ' + me.cellCls;
        };
        var tdLabels = me.eventEl.query('tbody td label');
        var specialDays = new Ext.util.HashMap();
        specialDays = me.getSpecialDay();
        for (; i < me.numDays; ++i) {
            tdLabels[i].innerHTML = ""
            textNodes[i].kind = "";
            textNodes[i].remark = "";
            if (i < startingPos) {
                html = (++prevStart);
                cls = me.prevCls;
            } else if (i >= days) {
                html = (++extraDays);
                cls = me.nextCls;
            } else {
                html = i - startingPos + 1;
                cls = me.activeCls;
            }
            textNodes[i].innerHTML = html;
            current.setDate(current.getDate() + 1);
            var currentFmt = eDate.format(current, 'Y-m-d');
            var day = specialDays.get(currentFmt);
            if(Ext.typeOf(day)!='undefined'){
                textNodes[i].kind = day.kind;
                textNodes[i].remark = day.remark;
                if(day.kind == 1) {
                    tdLabels[i].style.color = 'green';
                    tdLabels[i].innerHTML = "休";
                }else if(day.kind == 2){
                    tdLabels[i].style.color = 'red';
                    tdLabels[i].innerHTML = "班"
                }else{
                }
            }
            setCellClass(cells[i], cls);
        }
        me.monthBtn.setText(Ext.Date.format(date, me.monthYearFormat));
        // 消除加载层
        Ext.defer(function() {
            me.setLoading(false);
        }, 100);
    }
})