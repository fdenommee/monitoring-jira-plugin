(function () {
    var gadget = AJS.Gadget({
        baseUrl: GadgetBaseUrl,
        useOauth: "/rest/gadget/1.0/currentUser",
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
                        AJS.gadget.fields.projectPicker(gadget,"projectId", args.options),
                        AJS.gadget.fields.nowConfigured()
                    ]
                };
            },
            args: function(){
                return [
                    {
                        key: "options",
                        ajaxOptions:  "/rest/gadget/1.0/filtersAndProjects?showFilters=true"
                    }
                ];
            }()
        },
        view: {
            onResizeAdjustHeight: true,
            enableReload: true,
            template: function (args) {

                var gadget = this;            
                gadgets.window.setTitle(gadget.getMsg("gadget.monitoring.user.title"));
                
                gadget.getView().empty();
                gadget.getView().append(AJS.$("<div id='gadget_monitoring_user'/>"));

                var projectId = gadget.getPref("projectId");
                
            	AJS.$.ajax({
            		url: "/rest/monitor/1.0/usershtml",
                    data: ({projectId: projectId}),
            		dataType: "json",
            		cache: false,
            		success: function(data) {
                        AJS.$("#gadget_monitoring_user").html(data.body);
                        gadget.resize();
            		},
            		error: function(xhr, textStatus, errorThrown) {
                        alert("readyState: " + xhr.readyState + "\nstatus: " + xhr.status);
                        alert("responseText: " + xhr.responseText);
                    }
                });
                gadget.resize();
            }
        }
    });
})();
