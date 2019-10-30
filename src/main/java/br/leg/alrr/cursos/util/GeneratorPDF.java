package br.leg.alrr.cursos.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import br.leg.alrr.cursos.model.Aluno;
import br.leg.alrr.cursos.model.Matricula;
import com.lowagie.text.pdf.BaseFont;

public class GeneratorPDF {

    private ByteArrayOutputStream baos;
    private HttpServletResponse response;
    private FacesContext context;
    private String path1;
    private String path2;
    private String certif;
    private String certifVerso;

    public GeneratorPDF() {
        this.context = FacesContext.getCurrentInstance();
        this.response = (HttpServletResponse) context.getExternalContext().getResponse();

    }

    public void certificado(Aluno aluno) throws IOException, DocumentException {

        certif = context.getExternalContext().getRealPath("/jasper/imagens/certificado.jpg");
        certifVerso = context.getExternalContext().getRealPath("/jasper/imagens/certificado_verso.jpg");
        
        String fonteGotham = context.getExternalContext().getRealPath("/jasper/FONTS/GothamNarrow-Book_0.otf");
        String fonteGothamNegrito = context.getExternalContext().getRealPath("/jasper/FONTS/Gotham-Bold_0.otf");
        BaseFont fonteNegrito = BaseFont.createFont(fonteGothamNegrito, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font negrito = new Font(fonteNegrito, 13);
        BaseFont fonteNormal = BaseFont.createFont(fonteGotham, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font normal = new Font(fonteNormal, 13);
        Font normalPequeno = new Font(fonteNormal, 10);

        // criação do objeto documento
        Document document = new Document();
        document.setPageSize(PageSize.A4.rotate());
        baos = new ByteArrayOutputStream();

        try {

            Image certificado = Image.getInstance(certif);
            certificado.scaleAbsolute(845, 600);
            certificado.setAbsolutePosition(0, 0);

            Image certificadoVerso = Image.getInstance(certifVerso);
            certificadoVerso.scaleAbsolute(845, 600);
            certificadoVerso.setAbsolutePosition(0, 0);

            PdfWriter.getInstance(document, baos);
            document.open();

            //inicio pagina frente
            document.add(certificado);
            ControllerTable.dadosCertificado(document, aluno);
            //fim pagina frente

            // inicio pagina verso
            Paragraph p1 = new Paragraph(" - " + aluno.getCurso().getNome(), normal);
            Paragraph p2 = new Paragraph("UNIDADE ", negrito);
            Paragraph p3 = new Paragraph(" - " + aluno.getCurso().getUnidade().getNome(), normal);
            Paragraph p4 = new Paragraph(" - " + aluno.getTurma().getCargaHoraria() + " Horas", normal);
            Paragraph p5 = new Paragraph(aluno.getCurso().getConteudoProgramatico(), normal);

            p1.setSpacingBefore(11);
            p2.setSpacingBefore(10);
            p3.setSpacingBefore(3);
            p4.setSpacingBefore(30);
            p5.setSpacingBefore(62);

            p1.setFirstLineIndent(10);
            p2.setFirstLineIndent(5);
            p3.setFirstLineIndent(10);
            p4.setFirstLineIndent(10);

            p5.setIndentationLeft(10);
            p5.setIndentationRight(10);
            p5.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(certificadoVerso);

            document.add(p1);
            document.add(p2);
            document.add(p3);
            document.add(p4);

            document.add(p5);

            // fim pagina verso
            document.close();

            response.reset();
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            response.setHeader("Content-disposition", "attachment; filename=certif.pdf");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            context.responseComplete();

        } catch (DocumentException e) {
            throw new IOException(e.getMessage());
        }

    }

    public void certificados(List<Aluno> alunos) throws IOException, DocumentException {

        certif = context.getExternalContext().getRealPath("/jasper/imagens/certificado.jpg");
        certifVerso = context.getExternalContext().getRealPath("/jasper/imagens/certificado_verso.jpg");

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

        // criação do objeto documento
        Document document = new Document();
        document.setPageSize(PageSize.A4.rotate());
        baos = new ByteArrayOutputStream();

        try {

            Image certificado = Image.getInstance(certif);
            certificado.scaleAbsolute(845, 600);
            certificado.setAbsolutePosition(0, 0);

            Image certificadoVerso = Image.getInstance(certifVerso);
            certificadoVerso.scaleAbsolute(845, 600);
            certificadoVerso.setAbsolutePosition(0, 0);

            PdfWriter.getInstance(document, baos);
            document.open();
            for (Aluno aluno : alunos) {
                // inicio pagina frente
                document.add(certificado);

                Paragraph pnome = new Paragraph(aluno.getNome(), negritoGrande);

                Paragraph p6 = new Paragraph("participou do Curso de " + aluno.getCurso().getNome() + ", realizado no período de " + formatador.format(aluno.getCurso().getDataDeInicio()), normal);
                Paragraph p7 = new Paragraph(" a " + formatador.format(aluno.getCurso().getDataDeTermino())
                        + ", na Unidade " + aluno.getCurso().getUnidade().getNome() + ", com carga horária total de " + aluno.getTurma().getCargaHoraria() + " Horas", normal);
                Paragraph p8 = new Paragraph("Boa Vista, " + formatador.format(data), normalPequena);

                // adicionando espaço antes ao paragrafo
                pnome.setSpacingBefore(220);
                p6.setSpacingBefore(10);
                p6.setSpacingAfter(2);
                p7.setSpacingAfter(10);

                // alinhamento do texto
                pnome.setAlignment(Element.ALIGN_CENTER);
                p6.setAlignment(Element.ALIGN_CENTER);
                p7.setAlignment(Element.ALIGN_CENTER);
                p8.setAlignment(Element.ALIGN_CENTER);

                document.add(pnome);
                document.add(p6);
                document.add(p7);
                document.add(p8);
                document.newPage();
                // fim pagina frente

                // inicio pagina verso
                // inicio pagina verso
                Paragraph p1 = new Paragraph(" - " + aluno.getCurso().getNome(), normal);
                Paragraph p2 = new Paragraph("UNIDADE ", negrito);
                Paragraph p3 = new Paragraph(" - " + aluno.getCurso().getUnidade().getNome(), normal);
                Paragraph p4 = new Paragraph(" - " + aluno.getTurma().getCargaHoraria() + " Horas", normal);
                Paragraph p5 = new Paragraph(aluno.getCurso().getConteudoProgramatico(), normal);

                p1.setSpacingBefore(11);
                p2.setSpacingBefore(10);
                p3.setSpacingBefore(3);
                p4.setSpacingBefore(30);
                p5.setSpacingBefore(62);

                p1.setFirstLineIndent(10);
                p2.setFirstLineIndent(5);
                p3.setFirstLineIndent(10);
                p4.setFirstLineIndent(10);

                p5.setIndentationLeft(10);
                p5.setIndentationRight(10);
                p5.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(certificadoVerso);

                document.add(p1);
                document.add(p2);
                document.add(p3);
                document.add(p4);

                document.add(p5);
                document.newPage();
                //fim pagina verso

            }

            document.close();

            response.reset();
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            response.setHeader("Content-disposition", "attachment; filename=certif.pdf");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            context.responseComplete();

        } catch (DocumentException e) {
            throw new IOException(e.getMessage());
        }

    }

    public void declaracaoMatricula(Aluno aluno) throws IOException {

        // criação do objeto documento
        Document document = new Document(PageSize.A4, 72, 72, 30, 72);
        baos = new ByteArrayOutputStream();

        try {

            Date data = new Date();
            Locale local = new Locale("pt", "BR");
            SimpleDateFormat formatador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", local);

            path1 = context.getExternalContext().getRealPath("/jasper/imagens/logo-rr.png");
            path2 = context.getExternalContext().getRealPath("/jasper/imagens/xlogo-header.png");
            //PdfWriter.getInstance(document,new FileOutputStream("Declaração de " + aluno.getNome()+ ".pdf"));
            PdfWriter.getInstance(document, baos);
            document.open();
            Image logorr = Image.getInstance(path1);
            Image logoale = Image.getInstance(path2);

            logorr.scaleAbsolute(59, 73);
            logorr.setAbsolutePosition(50, 760);

            logoale.scaleAbsolute(100, 49);
            logoale.setAbsolutePosition(470, 770);

            Font fonteNegrito = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            Font fonteItalico = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.ITALIC);
            Font fonteNormal = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);

            // adicionando um parágrafo ao documento
            Paragraph p = new Paragraph("ASSEMBLEIA LEGISLATIVA DO ESTADO DE RORAIMA");
            Paragraph p1 = new Paragraph("Amazônia: Patrimônio dos Brasileiros", fonteItalico);
            Paragraph p2 = new Paragraph("Escola do Legislativo - Cursos Preparatórios");
            Paragraph p3 = new Paragraph("DECLARAÇÃO DE MATRÍCULA", fonteNegrito);
            Paragraph p4 = new Paragraph("A Coordenação da Escola do Legislativo – "
                    + "Cursos Preparatórios declara que " + aluno.getNome() + ", "
                    + "portador(a) do CPF: " + aluno.getCpf() + ", "
                    + "está devidamente matriculado no curso de " + aluno.getTurma().getModulo().getCurso().getNome() + ", "
                    + "nos dias de " + aluno.getTurma().getDiasDaSemana() + ", no horário das " + aluno.getTurma().getHorario().getDescricao() + ", "
                    + "nesta Instituição de Ensino."
                    + "", fonteNormal);
            Paragraph p5 = new Paragraph("_________________________________________");
            Paragraph p6 = new Paragraph(aluno.getDiretor().getNome());
            Paragraph p7 = new Paragraph("Diretor(a) da Escola do Legislativo");
            Paragraph p8 = new Paragraph("Boa Vista, " + formatador.format(data));

            // adicionando espaço antes ao paragrafo
            p3.setSpacingBefore(60);
            p4.setSpacingBefore(90);
            p4.setSpacingAfter(20);
            p5.setSpacingBefore(60);
            p8.setSpacingBefore(70);
            //adicionando espaço entre linhas no paragrafo
            p4.setLeading(25);
            // adicionando recuo na primeira linha
            p4.setFirstLineIndent(30);;

            // alinhamento do texto
            p.setAlignment(Element.ALIGN_CENTER);
            p1.setAlignment(Element.ALIGN_CENTER);
            p2.setAlignment(Element.ALIGN_CENTER);
            p3.setAlignment(Element.ALIGN_CENTER);
            p4.setAlignment(Element.ALIGN_JUSTIFIED);
            p5.setAlignment(Element.ALIGN_CENTER);
            p6.setAlignment(Element.ALIGN_CENTER);
            p7.setAlignment(Element.ALIGN_CENTER);
            p8.setAlignment(Element.ALIGN_RIGHT);

            document.add(p);
            document.add(p1);
            document.add(p2);
            document.add(p3);
            document.add(p4);
            document.add(p5);
            document.add(p6);
            document.add(p7);
            document.add(p8);

            document.add(logorr);
            document.add(logoale);

            document.addSubject("Declaração de Matrícula");
            document.addKeywords("al.rr.leg.br");
            document.addCreator("ALE-RR");
            document.addAuthor("ALE-RR");

            document.close();

            response.reset();
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            response.setHeader("Content-disposition", "attachment; filename=declaracao.pdf");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            context.responseComplete();

        } catch (DocumentException e) {
            throw new IOException(e.getMessage());
        }

    }

    public void declaracaoConclusaoCurso(Aluno aluno) throws IOException {

        // criação do objeto documento
        Document document = new Document(PageSize.A4, 72, 72, 30, 72);
        baos = new ByteArrayOutputStream();

        try {

            Date data = new Date();
            Locale local = new Locale("pt", "BR");
            SimpleDateFormat formatador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", local);
            SimpleDateFormat formatDataLocal = new SimpleDateFormat("dd/MM/yyyy", local);

            path1 = context.getExternalContext().getRealPath("/jasper/imagens/logo-rr.png");
            path2 = context.getExternalContext().getRealPath("/jasper/imagens/xlogo-header.png");
            //PdfWriter.getInstance(document,new FileOutputStream("Declaração de " + aluno.getNome()+ ".pdf"));
            PdfWriter.getInstance(document, baos);
            document.open();
            Image logorr = Image.getInstance(path1);
            Image logoale = Image.getInstance(path2);

            logorr.scaleAbsolute(59, 73);
            logorr.setAbsolutePosition(50, 760);

            logoale.scaleAbsolute(100, 49);
            logoale.setAbsolutePosition(470, 770);

            Font fonteNegrito = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            Font fonteItalico = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.ITALIC);
            Font fonteNormal = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);

            // adicionando um parágrafo ao documento
            Paragraph p = new Paragraph("ASSEMBLEIA LEGISLATIVA DO ESTADO DE RORAIMA");
            Paragraph p1 = new Paragraph("Amazônia: Patrimônio dos Brasileiros", fonteItalico);
            Paragraph p2 = new Paragraph("Escola do Legislativo - Cursos Preparatórios");
            Paragraph p3 = new Paragraph("DECLARAÇÃO DE CONCLUSÃO DE CURSO", fonteNegrito);
            Paragraph p4 = new Paragraph("A Coordenação da Escola do Legislativo – "
                    + "Cursos Preparatórios declara para os fins de comprovação de conclusão de curso que " + aluno.getNome() + ", "
                    + "portador(a) do CPF: " + aluno.getCpf() + ", "
                    + "concluiu o curso de " + aluno.getCurso().getNome() + ", "
                    + "com carga horária de " + aluno.getCurso().getCargaHoraria() + "h, realizado entre o período de " + formatDataLocal.format(aluno.getCurso().getDataDeInicio()) + " a "
                    + formatDataLocal.format(aluno.getCurso().getDataDeTermino()) + ", nesta Instituição de Ensino."
                    + "", fonteNormal);
            Paragraph p5 = new Paragraph("_________________________________________");
            Paragraph p6 = new Paragraph(aluno.getDiretor().getNome());
            Paragraph p7 = new Paragraph("Diretor(a) da Escola do Legislativo");
            Paragraph p8 = new Paragraph("Boa Vista, " + formatador.format(data));

            // adicionando espaço antes ao paragrafo
            p3.setSpacingBefore(60);
            p4.setSpacingBefore(90);
            p4.setSpacingAfter(20);
            p5.setSpacingBefore(60);
            p8.setSpacingBefore(70);
            //adicionando espaço entre linhas no paragrafo
            p4.setLeading(25);
            // adicionando recuo na primeira linha
            p4.setFirstLineIndent(30);;

            // alinhamento do texto
            p.setAlignment(Element.ALIGN_CENTER);
            p1.setAlignment(Element.ALIGN_CENTER);
            p2.setAlignment(Element.ALIGN_CENTER);
            p3.setAlignment(Element.ALIGN_CENTER);
            p4.setAlignment(Element.ALIGN_JUSTIFIED);
            p5.setAlignment(Element.ALIGN_CENTER);
            p6.setAlignment(Element.ALIGN_CENTER);
            p7.setAlignment(Element.ALIGN_CENTER);
            p8.setAlignment(Element.ALIGN_RIGHT);

            document.add(p);
            document.add(p1);
            document.add(p2);
            document.add(p3);
            document.add(p4);
            document.add(p5);
            document.add(p6);
            document.add(p7);
            document.add(p8);

            document.add(logorr);
            document.add(logoale);

            document.addSubject("Declaração de Conclusão de Curso");
            document.addKeywords("al.rr.leg.br");
            document.addCreator("ALE-RR");
            document.addAuthor("ALE-RR");

            document.close();

            response.reset();
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            response.setHeader("Content-disposition", "attachment; filename=declaracaoConclusao.pdf");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            context.responseComplete();

        } catch (DocumentException e) {
            throw new IOException(e.getMessage());
        }

    }

    public void declaracaoTodosCursosConcluidos(List<Matricula> matriculas) throws IOException {

        path1 = context.getExternalContext().getRealPath("/jasper/imagens/logo-rr.png");
        path2 = context.getExternalContext().getRealPath("/jasper/imagens/xlogo-header.png");

        // criação do objeto documento
        Document document = new Document();
        baos = new ByteArrayOutputStream();

        try {
            Image logorr = Image.getInstance(path1);
            Image logoale = Image.getInstance(path2);

            logorr.scaleAbsolute(59, 73);
            logorr.setAbsolutePosition(50, 760);

            logoale.scaleAbsolute(100, 49);
            logoale.setAbsolutePosition(470, 770);
            Date data = new Date();
            PdfWriter.getInstance(document, baos);
            document.open();
            document.add(logorr);
            document.add(logoale);
            PdfPTable table = ControllerTable.criarCabecalho();
            ControllerTable.preencherDados(document, table, matriculas);

            document.close();

            response.reset();
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            response.setHeader("Content-disposition", "attachment; filename=declaracaoConclusao.pdf");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            context.responseComplete();

        } catch (DocumentException e) {
            throw new IOException(e.getMessage());
        }

    }
}
