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
                var infoLink="<!-- Collapsable Card -->\n" +
                    "   <div class=\"card shadow mb-4\">\n" +
                    "   <!-- Card Header - Accordion -->\n" +
                    "   <a href=\"#collapseCardExample\" class=\"d-block card-header py-3\" data-toggle=\"collapse\" role=\"button\" aria-expanded=\"true\" aria-controls=\"collapseCardExample\">\n" +
                    "        <h6 class=\"m-0 font-weight-bold text-primary\">link acortado</h6>\n" +
                    "   </a>\n" +
                    "   <!-- Card Content - Collapse -->\n" +
                    "       <div class=\"collapse show\" id=\"collapseCardExample\">\n" +
                    "       <div class=\"card-body\">\n" +
                    "<h6>Su nuevo link es <a href='"+html+"' class='link-previews'>"+
                    html+
                    "</a></h6>"+
                    "<br>"+
                    "<br>"+
                    "<h6>Puede acceder al siguiente link o escanear el siguiente codigo:</h6> <a href='"+html+"'>Estadisticas</a>"
                    +"<div id=\"qrcode\" name=\"qrcode\" style='margin-right: 50px'>"
                    +"</div>"+
                    "<script type=\"text/javascript\">\n" +
                    "    new QRCode(document.getElementById(\"qrcode\"), html);\n" +
                    "</"+"script>\"\n" +
                    "       </div>\n"+
                    "    </div>\n" +
                    "  </div>\n" +
                    " </div>"
                ;
                newlink.append(infoLink);
            }
        });
        e.preventDefault();
    });
</script>

<!-- Previews -->

<script>
    crossorigin="anonymous"
    src="https://polyfill.io/v3/polyfill.min.js?features=fetch">
</script>

<script src="https://cdn.jsdelivr.net/npm/react@16/umd/react.production.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/react-dom@16/umd/react-dom.production.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/styled-components@4/dist/styled-components.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@microlink/mql@latest/dist/mql.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@microlink/vanilla@latest/dist/microlink.min.js"></script>

<script>
    document.addEventListener('DOMContentLoaded', function (event) {

        // Example 2
        // Replace all elements with `link-preview` class
        // for microlink cards
        microlink('.link-previews')
        // Example 3
        // Replace all elements with `link-preview` class
        // for microlink cards, passing API specific options
        //microlink('.link-previews', { size: 'large' })
    })
</script>

<!-- Previews -->