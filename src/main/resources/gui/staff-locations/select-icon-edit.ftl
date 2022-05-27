<#import "/gui/staff-locations/staff-locations-commons.ftl" as stafflocationcommons/>
<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>

<@stafflocationcommons.stafflocationsSelectIcon
action="staff-locations/edit/icon?locationId=${locationId}"
icons=icons/>