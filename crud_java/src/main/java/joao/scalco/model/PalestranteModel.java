// Fixed PalestranteModel.java - Added toString method for ComboBox
package joao.scalco.model;

public class PalestranteModel {

    private Long id;
    private String nome;
    private String foto;
    private String descricao;
    private String especializacao;
    private String email;

    public PalestranteModel(){}

    public PalestranteModel(Long id, String nome, String foto, String descricao, String especializacao, String email) {
        this.id = id;
        this.nome = nome;
        this.foto = foto;
        this.descricao = descricao;
        this.especializacao = especializacao;
        this.email = email;
    }

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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEspecializacao() {
        return especializacao;
    }

    public void setEspecializacao(String especializacao) {
        this.especializacao = especializacao;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return id + " - " + nome;
    }
}