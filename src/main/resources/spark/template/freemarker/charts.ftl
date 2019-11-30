<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<script type="text/javascript">
    google.charts.load('current', {'packages':['corechart']});
    google.charts.setOnLoadCallback(drawChart);

    function drawChart() {

        var data = google.visualization.arrayToDataTable([
            ['Navegador', 'Clicks'],
            <#list browsers?keys as naveg>
            ['${naveg}',${browsers[naveg]}]
            <#if naveg_has_next>,</#if>
            </#list>
        ]);
        var options = {
            title: 'Browsers mas utilizados',
            pieHole: 0.3
        };
        var chart = new google.visualization.PieChart(document.getElementById('navChart'));
        chart.draw(data, options);
    }
</script>
<script type="text/javascript">
    google.charts.load('current', {'packages':['corechart']});
    google.charts.setOnLoadCallback(drawChart);

    function drawChart() {

        var data = google.visualization.arrayToDataTable([
            ['Sistema Operativo', 'Clicks'],
            <#list oss?keys as os>
            ['${os}',${oss[os]}]
            <#if os_has_next>,</#if>
            </#list>
        ]);
        var options = {
            title: 'Sistemas operativos mas utilizados',
            pieHole:0.3
        };
        var chart = new google.visualization.PieChart(document.getElementById('osChart'));
        chart.draw(data, options);
    }
</script>
<script>
    google.charts.load('current', {packages: ['corechart', 'bar']});
    google.charts.setOnLoadCallback(drawBasic);

    function drawBasic() {

        var data = new google.visualization.DataTable();
        data.addColumn('timeofday', 'Hora');
        data.addColumn('number', 'Clicks');

        data.addRows([
            <#list actPorHora?keys as act>
            [{v: [${act}, 0, 0], f: '${act} am'}, ${actPorHora[act]}]<#if act_has_next>,</#if>
            </#list>
        ]);

        var options = {
            title: 'Horas más activas',
            hAxis: {
                title: 'Hora',
                format: 'h:mm a',
                viewWindow: {
                    min: [0, 00, 0],
                    max: [23, 00, 0]
                }
            },
            vAxis: {
                title: 'Clicks'
            }
        };

        var chart = new google.visualization.ColumnChart(
            document.getElementById('chart_div'));

        chart.draw(data, options);
    }
</script>

<div class="row">
    <!-- Area Chart -->
    <div class="col-xl-8 col-lg-10">
        <div class="card shadow mb-4">
            <!-- Card Header - Dropdown -->
            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                <h6 class="m-0 font-weight-bold text-primary">Uso en el tiempo</h6>
            </div>
            <!-- Card Body -->
            <div class="card-body">
                <div id="chart_div"></div>
            </div>
        </div>
    </div>


</div>

<div class="row">
    <!-- Pie Chart -->
    <div class="col-xl-4 col-lg-5">
        <div class="card shadow mb-4">
            <!-- Card Header - Dropdown -->
            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                <h6 class="m-0 font-weight-bold text-primary">Browsers mas usados</h6>
            </div>
            <!-- Card Body -->
            <div class="card-body">
                <div id="navChart"></div>
            </div>
        </div>
    </div>
    <!-- Pie Chart -->
    <div class="col-xl-4 col-lg-5">
        <div class="card shadow mb-4">
            <!-- Card Header - Dropdown -->
            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                <h6 class="m-0 font-weight-bold text-primary">Sistema Operativo</h6>
            </div>
            <!-- Card Body -->
            <div class="card-body">
                <div id="osChart"></div>
            </div>
        </div>
    </div>
</div>