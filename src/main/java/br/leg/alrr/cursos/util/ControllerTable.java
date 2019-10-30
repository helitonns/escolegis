package br.leg.alrr.cursos.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import br.leg.alrr.cursos.model.Aluno;
import br.leg.alrr.cursos.model.Matricula;
import com.lowagie.text.pdf.BaseFont;
import java.io.IOException;
import javax.faces.context.FacesContext;

public class ControllerTable {

    static Font fonteNegrito = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
    static Font fonteItalico = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.ITALIC);
    static Font fonteNormal = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);

    public static void dadosCertificado(Document document, Aluno aluno) throws DocumentException, IOException {

        FacesContext context;
        context = FacesContext.getCurrentInstance();

        String fonteGotham = context.getExternalContext().getRealPath("/jasper/FONTS/GothamNarrow-Book_0.otf");
        String fonteGothamNegrito = context.getExternalContext().getRealPath("/jasper/FONTS/Gotham-Bold_0.otf");
        BaseFont fonteNegrito = BaseFont.createFont(fonteGothamNegrito, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font negrito = new Font(fonteNegrito, 13);
        Font negritoGrande = new Font(fonteNegrito, 16);
        BaseFont fonteNormal = BaseFont.createFont(fonteGotham, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font normal = new Font(fonteNormal, 13);
        Font normalPequena = new Font(fonteNormal, 10);

        Date data = new Date();
        Locale local = new Locale("pt", "BR");
        SimpleDateFormat formatador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", local);

        // adicionando um parágrafo ao documento
        Paragraph pnome = new Paragraph(aluno.getNome(), negritoGrande);

        Paragraph p4 = new Paragraph("participou do Curso de " + aluno.getCurso().getNome() + ", realizado no período de " + formatador.format(aluno.getCurso().getDataDeInicio()), normal);
        Paragraph p5 = new Paragraph(" a " + formatador.format(aluno.getCurso().getDataDeTermino())
                + ", na Unidade " + aluno.getCurso().getUnidade().getNome() + ", com carga horária total de " + aluno.getTurma().getCargaHoraria() + " Horas", normal);

        Paragraph p8 = new Paragraph("Boa Vista, " + formatador.format(data), normalPequena);
        // adicionando espaço antes ao paragrafo
        pnome.setSpacingBefore(220);
        p4.setSpacingBefore(10);
        p4.setSpacingAfter(2);
        p5.setSpacingAfter(10);

        // alinhamento do texto
        pnome.setAlignment(Element.ALIGN_CENTER);
        p4.setAlignment(Element.ALIGN_CENTER);
        p5.setAlignment(Element.ALIGN_CENTER);
        p8.setAlignment(Element.ALIGN_CENTER);

        document.add(pnome);
        document.add(p4);
        document.add(p5);
        document.add(p8);
        document.newPage();

    }

    public static PdfPTable criarCabecalho()
            throws DocumentException {
        PdfPTable table = new PdfPTable(new float[]{15f, 7f, 9f});
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);

        PdfPCell celulaCurso = new PdfPCell(new Phrase("CURSO", fonteNegrito));
        celulaCurso.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell celulaCargaHoraria = new PdfPCell(new Phrase("CARGA HORÁRIA", fonteNegrito));
        celulaCargaHoraria.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell celulaPeriodo = new PdfPCell(new Phrase("PERÍODO", fonteNegrito));
        celulaPeriodo.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(celulaCurso);
        table.addCell(celulaCargaHoraria);
        table.addCell(celulaPeriodo);

        return table;
    }

    public static void preencherDados(Document document, PdfPTable table,
            List<Matricula> matriculas) throws DocumentException {

        Date data = new Date();
        Locale local = new Locale("pt", "BR");
        SimpleDateFormat formatador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", local);
        SimpleDateFormat formatDataLocal = new SimpleDateFormat("dd/MM/yyyy", local);

        Paragraph p = new Paragraph("ASSEMBLEIA LEGISLATIVA DO ESTADO DE RORAIMA");
        Paragraph p1 = new Paragraph("Amazônia: Patrimônio dos Brasileiros", fonteItalico);
        Paragraph p2 = new Paragraph("Escola do Legislativo - Cursos Preparatórios");
        Paragraph p3 = new Paragraph("DECLARAÇÃO DE CONCLUSÃO DE CURSO", fonteNegrito);
        Paragraph p4 = new Paragraph("A Coordenação da Escola do Legislativo – "
                + "Cursos Preparatórios declara para os fins de comprovação de conclusão de curso que " + matriculas.get(0).getAluno().getNome() + ", "
                + "portador(a) do CPF: " + matriculas.get(0).getAluno().getCpf() + ", "
                + "concluiu os cursos abaixo relacionados nesta Instituição de Ensino."
                + "", fonteNormal);
        Paragraph p5 = new Paragraph("_________________________________________");
        Paragraph p6 = new Paragraph(matriculas.get(0).getAluno().getDiretor().getNome());
        Paragraph p7 = new Paragraph("Diretor(a) da Escola do Legislativo");
        Paragraph p8 = new Paragraph("Boa Vista, " + formatador.format(data));

        p.setAlignment(Element.ALIGN_CENTER);
        p1.setAlignment(Element.ALIGN_CENTER);
        p2.setAlignment(Element.ALIGN_CENTER);
        p3.setAlignment(Element.ALIGN_CENTER);
        p4.setAlignment(Element.ALIGN_JUSTIFIED);
        p5.setAlignment(Element.ALIGN_CENTER);
        p6.setAlignment(Element.ALIGN_CENTER);
        p7.setAlignment(Element.ALIGN_CENTER);
        p8.setAlignment(Element.ALIGN_RIGHT);
        // adicionando espaço antes ao paragrafo
        p3.setSpacingBefore(60);
        p4.setSpacingBefore(70);
        p4.setSpacingAfter(20);
        p5.setSpacingBefore(60);
        p8.setSpacingBefore(70);

        //adicionando espaço entre linhas no paragrafo
        p4.setLeading(25);
        // adicionando recuo na primeira linha
        p4.setFirstLineIndent(30);;

        if (document.isOpen()) {
            for (Matricula m : matriculas) {
                PdfPCell celula1 = new PdfPCell(new Phrase(m.getCurso().getNome(), fonteNormal));
                PdfPCell celula2 = new PdfPCell(new Phrase(String.valueOf(m.getCurso().getCargaHoraria()), fonteNormal));
                PdfPCell celula3 = new PdfPCell(new Phrase(formatDataLocal.format(m.getCurso().getDataDeInicio()) + " - " + formatDataLocal.format(m.getCurso().getDataDeTermino()), fonteNormal));
                celula1.setHorizontalAlignment(Element.ALIGN_CENTER);
                celula2.setHorizontalAlignment(Element.ALIGN_CENTER);
                celula3.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(celula1);
                table.addCell(celula2);
                table.addCell(celula3);
            }
            document.add(p);
            document.add(p1);
            document.add(p2);
            document.add(p3);
            document.add(p4);
            document.add(table);
            document.add(p5);
            document.add(p6);
            document.add(p7);
            document.add(p8);
        }
    }
}
