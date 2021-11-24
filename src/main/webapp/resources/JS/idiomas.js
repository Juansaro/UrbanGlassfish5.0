$.getJSON('JS/contenidoidiomas.json', function(json){
    $('.translate').click(function(){
        let lang = $(this).attr('id');
        $('.lang').each(function(index,value){
            $(this).text(json[lang][$(this).attr('key')]);
        })//Cierra each
    })//Cierra click
})//Cierra json

$.getJSON('JS/contenidoidiomasVentas.json', function(json){
    $('.translate').click(function(){
        let lang = $(this).attr('id');
        $('.lang').each(function(index,value){
            $(this).text(json[lang][$(this).attr('key')]);
        })//Cierra each
    })//Cierra click
})//Cierra json

// id = "hola"
// key = "hola"