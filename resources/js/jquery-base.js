/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
jQuery(function(){
                jQuery("input:submit").button();
                jQuery("input:text, input:password, textarea").addClass("ui-widget-content")
                                                               .addClass("ui-corner-all")
                                                               .addClass("ui-inputfield")
                                                               .width(300)
                                                               .css("padding","3px");
 
                jQuery("select").addClass("ui-widget-content")
                                .addClass("ui-corner-all")
                                .addClass("ui-inputfield") ;
                                                              
                                            
             //calendário nas classes Datepicker
                jQuery("input:text.ui-state-default").css("background", "none")
                                                     .hover("",function() {jQuery(this).css("color", "black");} ) ;

                jQuery(".ui-column-filter").width(102).css("padding","0px").css("background","white");  

                jQuery("#menu\\:"+jQuery("#tabId").val()).css("display", "block");
                                                               
                }
             )

// Funcões para o ajaxloading
function showWorkingIndicator(data) {
    showIndicatorRegion(data, "workingIndicator");
}
function showIndicatorRegion(data, regionId) {
    if ( data.status == "begin") {
        showElement(regionId);
    } else if (data.status == "success") {
        hideElement(regionId);
    }
}
function showElement(id) {
    document.getElementById(id).style.display = "inline";
}

function hideElement(id) {
    document.getElementById(id).style.display = "none";
}
