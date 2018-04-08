/**
 * 会话监视任务,提醒用户,他们的会话将在60秒并提供到期
  *选择继续工作或注销。如果倒计时计时器到期,用户自动
  *注销。
 */
Ext.define('Util.SessionMonitor', {
  singleton: true,
  interval: 1000 * 10,  // run every 10 seconds.
  lastActive: null,
  maxInactive: 1000 * 60 * 15,  // 15 minutes of inactivity allowed; set it to 1 for testing.
  remaining: 0,
  ui: Ext.getBody(),
  
  /**
   * 对话框显示过期消息和倒计时定时器。
   */
  window: Ext.create('Ext.window.Window', {
    bodyPadding: 5,
    closable: false,
    closeAction: 'hide',
    modal: true,
    resizable: false,
    title: '会话超时提醒',
    width: 325,
    items: [{
      xtype: 'container',
      frame: true,
      html: "您的会话空闲15分钟后将自动失效。如果你的会话过期,任何未保存的数据将丢失,您的会话将自动注销 ！</br>如果你想继续工作,点击“继续”按钮。</br></br>"    
    },{
      xtype: 'label',
      text: ''
    }],
    buttons: [{
      text: '继续',
      handler: function() {
        Ext.TaskManager.stop(Util.SessionMonitor.countDownTask);
        Util.SessionMonitor.window.hide();
        Util.SessionMonitor.start();
        // 服务器端更新会话。
        Ext.Ajax.request({
          url: 'login/check'
        });
      }
    },{
      text: '注销',
      action: 'logout',
      handler: function() {
        Ext.TaskManager.stop(Util.SessionMonitor.countDownTask);
        Util.SessionMonitor.window.hide();
        
        // 找到并调用应用程序的“注销”按钮。
        Ext.ComponentQuery.query('button#logout')[0].fireEvent('click',Ext.ComponentQuery.query('button#logout')[0]);
      }
    }]
  }),

 
 
  constructor: function(config) {
    var me = this;
   
    // 会话监视任务
    this.sessionTask = {
      run: me.monitorUI,
      interval: me.interval,
      scope: me
    };

    // 会话超时任务,显示一个60秒的倒计时
    // 消息提醒用户,他们的会话即将到期。
    this.countDownTask = {
      run: me.countDown,
      interval: 1000,
      scope: me
    };
  },
 
 
  /**
   * 简单的方法来注册mousemove和keydown事件.
   */
  captureActivity : function(eventObj, el, eventOptions) {
    this.lastActive = new Date();
  },


  /**
   *  监控UI,以确定如果你超过了不活动的阈值。
   */
  monitorUI : function() {
    var now = new Date();
    var inactive = (now - this.lastActive);
    if (inactive >= this.maxInactive) {
      this.stop();

      this.window.show();
      this.remaining = 60;  // seconds remaining.
      Ext.TaskManager.start(this.countDownTask);
    }
  },

 
  /**
   * 启动会话计时器任务和寄存器鼠标/键盘活动事件监视器。
   */
  start : function() {
    this.lastActive = new Date();

    this.ui = Ext.getBody();

    this.ui.on('mousemove', this.captureActivity, this);
    this.ui.on('keydown', this.captureActivity, this);
        
    Ext.TaskManager.start(this.sessionTask);
  },
 
  /**
   * 停止会话计时器任务和注销鼠标/键盘活动事件监视器。
   */
  stop: function() {
    Ext.TaskManager.stop(this.sessionTask);
    this.ui.un('mousemove', this.captureActivity, this);  //  always wipe-up after yourself...
    this.ui.un('keydown', this.captureActivity, this);
  },
 
 
  /**
   * 倒计时功能更新消息标签用户对话框显示
   * 会话过期之前剩下的秒。如果到期调用注销事件。
   */
  countDown: function() {
    this.window.down('label').update('您的会话'+  this.remaining +'秒后自动注销！');
    
    --this.remaining;

    if (this.remaining < 0) {
      this.window.down('button[action="logout"]').handler();
    }
  }
 
});