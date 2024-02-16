package shop.mtcoding.blog.reply;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.board.BoardResponse;
import shop.mtcoding.blog.user.User;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ReplyRepository {
    private final EntityManager em;
    public List<BoardResponse.ReplyDTO> findByBoardId(int boardId, User sessionUser){
        String q= """

                select rt.id, rt.user_id, rt.comment, ut.username from reply_tb rt inner join user_tb ut on rt.user_id = ut.id where rt.board_id =?
                """;

        Query query = em.createNativeQuery(q); //ReplyDTO.class를 못받는이유는 엔티티가 아니라서 지원을 안해줌
        query.setParameter(1, boardId);

        List<Object[]> rows =query.getResultList();

        return rows.stream().map(row -> new BoardResponse.ReplyDTO(row, sessionUser)).toList(); //stream을 사용하여 데이터를 뿌린 후 map을 사용하여 ReplyDTO에 넣어서 리스트에 넣어준다
    }

    @Transactional
    public void save(ReplyRequest.WriteDTO requestDTO, int userId){
        Query query = em.createNativeQuery("insert into reply_tb(comment, board_id, user_id, created_at) values(?, ?, ?, now())");
        query.setParameter(1, requestDTO.getComment());
        query.setParameter(2, requestDTO.getBoardId());
        query.setParameter(3, userId);

        query.executeUpdate();
    }

    @Transactional
    public void deleteById(int id) {
        String q="delete from reply_tb where id=?";
        Query query = em.createNativeQuery(q);
        query.setParameter(1, id);

        query.executeUpdate();
    }

    public Reply findById(int id){
        String q="Select*from reply_tb where id=?";
        Query query = em.createNativeQuery(q, Reply.class);
        query.setParameter(1, id);

        try {
            return (Reply) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
