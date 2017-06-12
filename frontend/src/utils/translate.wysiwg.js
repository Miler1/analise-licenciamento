var WysiwygTranslator = {
    
    translate: function() {

        setTimeout(function(){

            $("[title='Bold']").prop('title','Negrito');
            $("[title='Italic']").prop('title','Itálico');
            $("[title='Underline']").prop('title','Sublinhado');
            $("[title='Strikethrough']").prop('title','Tachado');
            $("[title='Subscript']").prop('title','Subscrito');
            $("[title='Superscript']").prop('title','Sobrescrito');
            $("[title='Left align']").prop('title','Alinhar a esquerda');
            $("[title='Center align']").prop('title','Centralizar');
            $("[title='Right align']").prop('title','Alinhar a direita');
            $("[title='Block Justify']").prop('title','Justificar');
            $("[title='Insert Ordered List']").prop('title','Lista ordenada');
            $("[title='Insert Unordered List']").prop('title','Lista não ordenada');
            $("[title='Outdent']").prop('title','Desindentar');
            $("[title='Indent']").prop('title','Identar');
            $("[title='Undo']").prop('title','Desfazer');
            $("[title='Redo']").prop('title','Refazer');
            $("[title='Font Color']").prop('title','Cor da fonte');
            $("[title='Insert hyperlink']").prop('title','Inserir link');

            $("option[value='number:1']").text('Muito pequeno');
            $("option[value='number:2']").text('Pequeno');
            $("option[value='number:3']").text('Normal');
            $("option[value='number:4']").text('Grande');
            $("option[value='number:5']").text('Muito grande');
            $("option[value='number:6']").text('Extra grande');
            $("option[value='number:7']").text('Gigante');

        
        },100);        

    }

};

exports.utils.WysiwygTranslator = WysiwygTranslator;