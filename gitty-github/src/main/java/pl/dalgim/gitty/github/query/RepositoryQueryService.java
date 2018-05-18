package pl.dalgim.gitty.github.query;

/**
 * Interface for creating Repository queries.
 *
 * @author Mateusz Dalgiewicz
 */
public interface RepositoryQueryService {

  /**
   * Function returns domain instance of Repository data.
   * @param params Search parameters
   * @return {@link RepositoryInfo} Repository data.
   */
  RepositoryInfo find(RepositorySearchParams params);

}
