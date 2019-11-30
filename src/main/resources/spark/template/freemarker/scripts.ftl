<!-- Bootstrap core JavaScript-->
<script src="/vendor/jquery/jquery.min.js"></script>
<script src="/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

<!-- Core plugin JavaScript-->
<script src="/vendor/jquery-easing/jquery.easing.min.js"></script>
<script
        src="https://code.jquery.com/jquery-3.4.1.min.js"
        integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo="
        crossorigin="anonymous"></script>

<!-- Custom scripts for all pages-->
<script src="/js/sb-admin-2.min.js"></script>

<!-- Page level plugins -->
<script src="/vendor/chart.js/Chart.min.js"></script>

<!-- Page level custom scripts -->
<script src="/js/demo/chart-area-demo.js"></script>
<script src="/js/demo/chart-pie-demo.js"></script>
<script src="/js/qrcode.min.js"></script>
<!--<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>-->

<script>
    //QR Gen
    var optUrl;
    $(document).on('submit', '#acortar', function(e) {
        var url = "url=" + $("#url").val();
        $.ajax({
            url: $(this).attr('action'),
            type: $(this).attr('method'),
            data: url,
            success: function(html) {
                var newlink = $("#newlink");
                optUrl = html.replace("/r/","/stats/");
                var infoLink="<h6>Su nuevo link es <a href='"+html+"'>"+
                    html+
                    "</a></h6>"+
                    "<br>"+
                    "<br>"+
                    "<h6>Si esta registrado y desea revisar las estadisticas de su enlace puede acceder al siguiente link o escanear el siguiente codigo:</h6> <a href='"+optUrl+"'>Estadisticas</a>"
                    +"<div id=\"qrcode\" name=\"qrcode\" style='margin-right: 50px'>"
                    +"</div>"+
                    "<script type=\"text/javascript\">\n" +
                    "    new QRCode(document.getElementById(\"qrcode\"), {text: optUrl, width: 128, height: 128, colorDark :#000000, colorLight :#ffffff, correctLevel : QRCode.CorrectLevel.H});\n" +
                    "</"+"script>";
                newlink.append(infoLink);
            }
        });
        e.preventDefault();
    });
</script>


