AJS.gadget.fields.testIssuePicker = function(gadget, userprefField, userprefLabel, userprefDescription, options) {
    if (options == null) {
    	options = {options:[{label:gadget.getPref(userprefField),value:gadget.getPref(userprefField)}] };
    }
	if(!AJS.$.isArray(options.options)){
        options.options = [options.options];
    }
    return {
        userpref: userprefField,
        label: gadget.getMsg(userprefLabel),
        description:gadget.getMsg(userprefDescription),
        type: "select",
        selected: gadget.getPref(userprefField),
		options: [{label:"Issue 1",value:"10000"},{label:"Issue 2",value:"10010"},{label:"Issue 3",value:"10020"},{label:"Issue 4",value:"10021"},{label:"Issue 5",value:"10022"}] 
    };
};

(function () {
    var gadget = AJS.Gadget({
        baseUrl: GadgetBaseUrl,
        config: {
            descriptor: function(args) {
                var gadget = this;
                gadgets.window.setTitle(gadget.getMsg("gadget.monitoring.user.title"));

                return {
                    theme : function() {
                        if (gadgets.window.getViewportDimensions().width < 450) {
                            return "gdt top-label";
                        } else {
                            return "gdt";
                        }
                    }(),
                    fields: [
                        AJS.gadget.fields.testIssuePicker(gadget,"issueId","Issue","Id. de l''Issue "),
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
                gadget.getView().append(AJS.$("<div id='gadget_monitoring_user'/>"));

                var issueId = gadget.getPref("issueId");
                var cacheBuster = new Date().getTime();

            	AJS.$.ajax({
            		url: gadget.getBaseUrl() + "/secure/MonitorCFRefresh.jspa",
                    data: ({decorator: 'none', issueId: issueId, cacheBuster: cacheBuster}),
            		dataType: "html",
            		cache: false,
            		success: function(data) {
            			AJS.$("#gadget_monitoring_user").html(data);
                        gadget.resize();
            		},
            		error: function(xhr, textStatus, errorThrown) {
            			alert(textStatus);
            			alert(errorThrown);
            		}
            	});
                gadget.resize();

            }
        }
    });
})();
