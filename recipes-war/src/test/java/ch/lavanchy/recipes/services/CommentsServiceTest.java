package ch.lavanchy.recipes.services;

import ch.lavanchy.recipes.business.CommentsBusinessLocal;
import ch.lavanchy.recipes.data.Comment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class CommentsServiceTest {
    @Mock
    private CommentsBusinessLocal commentsBusiness;

    @InjectMocks
    private CommentsService testee = new CommentsService();

    @Test
    public void testGetCommentsNoId() {
        final Response response = testee.getComments(null);

        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void testGetCommentsNoResponse() {
        doReturn(Collections.<Comment> emptyList()).when(commentsBusiness).findCommentsForRecipe(anyLong());

        final Response response = testee.getComments(1L);

        assertThat(response.getStatus()).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    public void testGetCommentsSucess() {
        final List<Comment> responseContent = Arrays.asList(mock(Comment.class), mock(Comment.class));
        doReturn(responseContent).when(commentsBusiness).findCommentsForRecipe(anyLong());

        final Response response = testee.getComments(1L);

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }
}