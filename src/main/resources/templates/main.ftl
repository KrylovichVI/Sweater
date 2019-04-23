<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>
<div>
    <@l.logout/>
</div>

<div>
    <div>
        <form method="post">
            <input type="text" name="text" placeholder="Введите текст">
            <input type="tag" name="tag" placeholder="Введите тэг">
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <button type="submit">Добавить</button>
        </form>
    </div>
    <div>Cписок сообщений</div>
    <form method="get" action="/main">
        <input type="text" name="filter" value="${filter?ifExists}">
        <button type="submit">Найти</button>
    </form>
    <#list messages as message>
        <div>
            <i>${message.id}</i>
            <strong>${message.text}</strong>
            <span>${message.tag}</span>
            <strong>${message.authorName}</strong>
        </div>
    <#else>
    No message
    </#list>
</div>
</@c.page>