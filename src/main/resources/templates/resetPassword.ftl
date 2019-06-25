<#import "parts/common.ftl" as c>

<@c.page>
<div>
    <h4>Create new password</h4>
</div>
    <#if message??>
    <div class="col-sm-4">
        <div class="alert alert-${messageType}" role="alert">
            ${message}
        </div>
    </div>
    </#if>
    <form method="post">
    <#if messageType?contains("success")>
        <div>
            <p>Please set your new password below.</p>
        </div>
        <div class="form-group row">
            <div class="col-sm-6">
                <input type="password"
                       name="password"
                       placeholder="New password"/>
            </div>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button type="submit" class="btn btn-primary mb-2">Reset password</button>
    </#if>
    </form>
</@c.page>