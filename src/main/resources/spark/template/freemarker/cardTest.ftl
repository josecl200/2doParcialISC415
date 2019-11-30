<#if usuario??>
<#list UrlCorta as Links>
<!-- Card -->
<div class="col-xl-3 col-md-6 mb-4">
    <div class="card border-left-warning shadow h-100 py-2">
        <div class="card-body">
            <div class="row no-gutters align-items-center">
                <div class="col mr-2">
                    <div class="text-xs font-weight-bold text-warning text-uppercase mb-1">${link}</div>
                    <div class="h5 mb-0 font-weight-bold text-gray-800">${linkorig}

                        <button type="button stats" class=" d-sm-inline-block btn btn-sm btn-primary shadow-sm">
                            <i class="fas fa-download fa-sm text-white-50"></i> Estadisticas</button>

                        <button type="button delete" class=" d-sm-inline-block btn btn-sm btn-primary shadow-sm">
                            <i class="fas fa-download fa-sm text-white-50"></i> Borrar Link</button>

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
<#else>
    <p>
        Peixe Cascudo
    </p>

</#if>

<#if usuario??>
    <#list Usuario as userlist>
        <!-- Card -->
        <div class="col-xl-3 col-md-6 mb-4">
            <div class="card border-left-warning shadow h-100 py-2">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-warning text-uppercase mb-1">${link}</div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">${linkorig}

                                <#if usuario??>
                                <button type="button upgrade" class=" d-sm-inline-block btn btn-sm btn-primary shadow-sm">
                                    <i class="fas fa-download fa-sm text-white-50"></i> Promover</button>
                                <#else>

                                <button type="button downgrade" class=" d-sm-inline-block btn btn-sm btn-primary shadow-sm">
                                    <i class="fas fa-download fa-sm text-white-50"></i> Destituir</button>
                                </#if>

                                <button type="button delete" class=" d-sm-inline-block btn btn-sm btn-primary shadow-sm">
                                    <i class="fas fa-download fa-sm text-white-50"></i> Borrar Link</button>

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
<#else>
    <p>
        Peixe Cascudo
    </p>

</#if>