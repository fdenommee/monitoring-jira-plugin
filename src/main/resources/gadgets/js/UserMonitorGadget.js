(function () {
    var gadget = AJS.Gadget({
        baseUrl: "__ATLASSIAN_BASE_URL__",
        config: {
            descriptor: function(args) {
                var gadget = this;
                gadgets.window.setTitle("__MSG_gadget.monitoring.user.title__");

                return {
                    theme : function() {
                        if (gadgets.window.getViewportDimensions().width < 450) {
                            return "gdt top-label";
                        } else {
                            return "gdt";
                        }
                    }(),
                    fields: [
                        AJS.gadget.fields.nowConfigured()
                    ]
                };
            }
        },
        view: {
            onResizeAdjustHeight: true,
            enableReload: true,
            template: function (args) {

                var gadget = this;            
                gadgets.window.setTitle(gadget.getMsg("gadget.monitoring.user.title"));
                
                gadget.getView().empty();
                gadget.getView().append(AJS.$("<div id='gadget_monitoring_user'/>").html("Hello"));

                gadget.resize();

            }
        }
    });
})();