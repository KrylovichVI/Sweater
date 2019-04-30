<#macro login path isRegisterForm>
<form action="${path}" method="post">
    <div class="form-group row">
        <label class="col-sm-2 col-form-label">User Name :</label>
            <div class="col-sm-6">
                <input type="text" name="username" class="form-control" placeholder="User name"/>
            </div>
    </div>
    <div class="form-group row">
        <label class="col-sm-2 col-form-label"> Password :</label>
            <div class="col-sm-6">
                <input type="password" class="form-control" name="password" placeholder="Password"/>
            </div>
    </div>
    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <#if !isRegisterForm><a href="/registration">Add new user</a> </#if>
    <button type="submit" class="btn btn-primary mb-2"/><#if isRegisterForm>Create<#else>Sign In</#if>
</form>
</#macro>

<#macro logout>
<form action="/logout" method="post">
    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <button type="submit" class="btn btn-primary mb-2">Sign Out</button>
</form>
</#macro>

