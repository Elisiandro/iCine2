
package br.com.elisiandro.iCine.bean;

import br.com.elisiandro.iCine.entidade.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;


/**
 *
 * @author Elisiandro
 */
@ManagedBean(name = "loginMB")
@SessionScoped
public class LoginMB implements Serializable{
    
    
    private String login, senha;
    private Usuario usuarioLogado;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    public boolean estaLogado() {
        return (usuarioLogado != null);
    }

    public boolean eAdmin() {
        return (this.estaLogado() && this.getUsuarioLogado().isAdmin());
    }

    public String verificaLogin() {
        //Pega o contexto do JSF - serve agora e depois para mensagem.
        FacesContext contexto = FacesContext.getCurrentInstance();
        //Obtém o usuarioMB criado pelo servidor (nível de aplicação)
        UsuarioMB usuarioMB = (UsuarioMB) contexto.getExternalContext().getApplicationMap().get("usuarioMB");
        //A partir do usuarioMB do servidor, pegamos a lista de usuários cadastrados no sistema
        List<Usuario> listaUsuarios = usuarioMB.getListaUsuarios();

        for (Usuario usuario : listaUsuarios) {
            if (usuario.verificaLogin(login, senha)) {
                usuarioLogado = usuario;
                
                return "view/home.xhtml?faces-redirect=true";
            }
        }
        FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Login inválido!", "Usuário ou senha estão errados!");
        contexto.addMessage("idMensagem", mensagem);
        return ("");
        
        /*
            if (this.usuarioSelecionado.getUser().equalsIgnoreCase("admin") && this.usuarioSelecionado.getPass().equalsIgnoreCase("admin")) 
        {
            usuarioLogado = usuarioSelecionado;
            return "view/home.xhtml?faces-redirect=true";
        }
        
        RequestContext.getCurrentInstance().update("growl");
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuário ou Senha inválido!" ));        
        return "";*/
        
    }

    public String realizaLogout() {
        usuarioLogado = null;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return ("/login?faces-redirect=true");
    }
    
}
