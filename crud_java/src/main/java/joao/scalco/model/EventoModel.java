package joao.scalco.model;

import java.sql.Timestamp;

public class EventoModel {
    private Long id;
    private String nome;
    private String descricao;
    private Timestamp dataInicio;
    private Timestamp dataFim;
    private String local;
    private Long palestranteId;

    public EventoModel() {
    }

    public EventoModel(Long id, String nome, String descricao, Timestamp dataInicio, Timestamp dataFim, String local, Long palestranteId) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.local = local;
        this.palestranteId = palestranteId;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Timestamp getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Timestamp dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Timestamp getDataFim() {
        return dataFim;
    }

    public void setDataFim(Timestamp dataFim) {
        this.dataFim = dataFim;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Long getPalestranteId() {
        return palestranteId;
    }

    public void setPalestranteId(Long palestranteId) {
        this.palestranteId = palestranteId;
    }

    @Override
    public String toString() {
        return "Palestrante{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", dataIni='" + dataInicio + '\'' +
                ", dataFim='" + dataFim + '\'' +
                ", local='" + local + '\'' +
                ", palestranteId='" + palestranteId + '\'' +
                '}';
    }

}
