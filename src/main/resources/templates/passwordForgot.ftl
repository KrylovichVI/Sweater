<#import "parts/common.ftl" as c>

<@c.page>
<form method="post">
<div class="form-group row pt-2">
    <label class="col-sm-2 col-form-label"> Email :</label>
        <div class="col-sm-6">
            <input type="email"
                   name="email"
                   class="form-control"
                   placeholder="Email"/>
            <#if message??>
                <div>
                    ${message}
                </div>
            </#if>
        </div>
    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    <button type="submit" name="continue" class="btn btn-primary mb-2">Reset password</button>
</div>
</form>

</@c.page>