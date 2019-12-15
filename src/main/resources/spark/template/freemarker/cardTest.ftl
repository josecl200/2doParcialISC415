<#if urls??>
<#list urls as url>
<!-- Card -->
    <div class="card border-left-warning shadow h-100 py-2">
        <div class="card-body">
            <div class="row no-gutters align-items-center">
                <div class="col mr-2">
                    <div class="text-xs font-weight-bold text-warning text-uppercase mb-1"><a href="/r/${url.idAsb64()}">${url.idAsb64()}</a></div>
                    <div class="h5 mb-0 font-weight-bold text-gray-800"><a class="link-previews">${url.url_orig}</a>

                        <a href="/stats/${url.idAsb64()}" type="button stats" class=" d-sm-inline-block btn btn-sm btn-primary shadow-sm">
                            <i class="fas fa-download fa-sm text-white-50"></i> Estadisticas</a>

                        <form method="post" action="/delUrl/${url.idAsb64()}">
                            <input id="path" name="path" class="path" type="hidden" value=""/>
                            <button type="button submit" class=" d-sm-inline-block btn btn-sm btn-primary shadow-sm">
                                <i class="fas fa-download fa-sm text-white-50"></i> Borrar Link</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</#list>
</#if>

<#if users??>
    <#list users as user>
        <!-- Card -->
        <div class="col-xl-3 col-md-6 mb-4">
            <div class="card border-left-warning shadow h-100 py-2">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-warning text-uppercase mb-1">${user.nombre}</div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">${user.username}

                                <#if user.admin>
                                <form method="post" action="/adminRights/${user.id}">

                                    <button type="button downgrade submit" class=" d-sm-inline-block btn btn-sm btn-primary shadow-sm">
                                        <i class="fas fa-download fa-sm text-white-50"></i> Destituir</button>
                                </form>

                                <#else>
                                    <form method="post" action="/adminRights/${user.id}">
                                        <input id="path" type="hidden"/>
                                        <button type="button upgrade submit" class=" d-sm-inline-block btn btn-sm btn-primary shadow-sm">
                                            <i class="fas fa-download fa-sm text-white-50"></i> Promover</button>
                                    </form>

                                </#if>

                                <form method="post" action="/delUsr/${user.id}">
                                    <button type="button delete submit" class=" d-sm-inline-block btn btn-sm btn-primary shadow-sm">
                                        <i class="fas fa-download fa-sm text-white-50"></i> Borrar Usuario</button>
                                </form>

                            </div>
                        </div>
                        <div class="col-auto">
                            <i class="fas fa-clipboard-list fa-2x text-gray-300"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </#list>

</#if>